package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.pedido.CadastrarPedidoUseCase;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Pedido;
import br.com.devsombrero.logistica.support.ClienteFixture;
import br.com.devsombrero.logistica.support.PedidoFixture;
import br.com.devsombrero.logistica.support.ProdutoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarPedidoUseCaseTest {

    private static final String CLIENTE_ID = PedidoFixture.CLIENTE_ID;
    private static final String PRODUTO_ID = PedidoFixture.PRODUTO_ID;

    @Mock
    private PedidoPersistenceOutputPort pedidoPersistence;
    @Mock
    private ClientePersistenceOutputPort clientePersistence;
    @Mock
    private ProdutoPersistenceOutputPort produtoPersistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private CadastrarPedidoUseCase useCase;

    @Test
    void deveCadastrarPedidoComItensDoProduto() {
        when(clientePersistence.buscarPorId(CLIENTE_ID)).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));
        when(produtoPersistence.buscarPorId(PRODUTO_ID)).thenReturn(Optional.of(ProdutoFixture.produtoSalvo()));
        when(pedidoPersistence.salvar(any(Pedido.class))).thenReturn(PedidoFixture.pedidoSalvo());

        RetornoPedido retorno = useCase.cadastrar(PedidoFixture.requisicaoCadastro());

        assertThat(retorno).isEqualTo(PedidoFixture.retornoPedido());
        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoPersistence).salvar(captor.capture());
        Pedido enviado = captor.getValue();
        assertThat(enviado.id()).isNull();
        assertThat(enviado.clienteId()).isEqualTo(CLIENTE_ID);
        assertThat(enviado.statusPedido()).isEqualTo(StatusPedido.PENDENTE);
        assertThat(enviado.observacao()).isEqualTo(PedidoFixture.OBSERVACAO);
        assertThat(enviado.itens()).containsExactly(PedidoFixture.itemSalvo());
        assertThat(enviado.valorTotal()).isEqualByComparingTo("39.80");
        verify(logger).info(CadastrarPedidoUseCase.class,
                "Pedido " + PedidoFixture.ID + " cadastrado com valor total 39.80");
    }

    @Test
    void deveRecusarPedidoNulo() {
        assertThatThrownBy(() -> useCase.cadastrar(null))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("Pedido não pode ser nulo");
        verifyNoInteractions(clientePersistence, produtoPersistence, pedidoPersistence);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void deveRecusarClienteInvalido(String clienteId) {
        Pedido pedido = new Pedido(null, clienteId, null, null, null, null,
                List.of(PedidoFixture.itemSolicitado()), null, null);

        assertThatThrownBy(() -> useCase.cadastrar(pedido))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O pedido deve possuir um cliente");
        verifyNoInteractions(clientePersistence, produtoPersistence, pedidoPersistence);
    }

    @Test
    void deveLancarQuandoClienteNaoEncontrado() {
        when(clientePersistence.buscarPorId(CLIENTE_ID)).thenReturn(Optional.empty());

        Pedido pedido = PedidoFixture.requisicaoCadastro();
        assertThatThrownBy(() -> useCase.cadastrar(pedido))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Cliente " + CLIENTE_ID + " não encontrado");
        verify(logger).warn(CadastrarPedidoUseCase.class, "Cliente " + CLIENTE_ID + " não encontrado");
        verifyNoInteractions(produtoPersistence, pedidoPersistence);
    }

    @Test
    void deveRecusarPedidoSemItens() {
        when(clientePersistence.buscarPorId(CLIENTE_ID)).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));
        Pedido pedido = new Pedido(null, CLIENTE_ID, null, null, null, null, null, null, null);

        assertThatThrownBy(() -> useCase.cadastrar(pedido))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O pedido deve possuir pelo menos um produto");
        verifyNoInteractions(produtoPersistence, pedidoPersistence);
    }

    @Test
    void deveLancarQuandoProdutoNaoEncontrado() {
        when(clientePersistence.buscarPorId(CLIENTE_ID)).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));
        when(produtoPersistence.buscarPorId(PRODUTO_ID)).thenReturn(Optional.empty());

        Pedido pedido = PedidoFixture.requisicaoCadastro();
        assertThatThrownBy(() -> useCase.cadastrar(pedido))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Produto " + PRODUTO_ID + " não encontrado");
        verifyNoInteractions(pedidoPersistence);
    }
}
