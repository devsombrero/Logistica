package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.pedido.AtualizarPedidoUseCase;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.ItemPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;
import br.com.devsombrero.logistica.domain.model.Produto;
import br.com.devsombrero.logistica.support.PedidoFixture;
import br.com.devsombrero.logistica.support.ProdutoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarPedidoUseCaseTest {

    private static final String ID = PedidoFixture.ID;
    private static final String PRODUTO_ID = PedidoFixture.PRODUTO_ID;

    @Mock
    private PedidoPersistenceOutputPort pedidoPersistence;
    @Mock
    private ProdutoPersistenceOutputPort produtoPersistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private AtualizarPedidoUseCase useCase;

    @Test
    void deveAtualizarSomenteObservacao() {
        when(pedidoPersistence.buscarPorId(ID)).thenReturn(Optional.of(PedidoFixture.pedidoSalvo()));
        when(pedidoPersistence.atualizar(eq(ID), any(Pedido.class))).thenAnswer(inv -> inv.getArgument(1));

        RetornoPedido retorno = useCase.atualizar(ID, alteracao("nova", List.of()));

        assertThat(retorno.observacao()).isEqualTo("nova");
        assertThat(retorno.itens()).containsExactly(PedidoFixture.itemSalvo());
        assertThat(retorno.valorTotal()).isEqualByComparingTo("39.80");
        verifyNoInteractions(produtoPersistence);
        verify(logger).info(AtualizarPedidoUseCase.class, "Pedido " + ID + " atualizado com valor total 39.80");
    }

    @Test
    void deveSubstituirItensBuscandoProdutos() {
        Produto produto = ProdutoFixture.produtoSalvo();
        when(pedidoPersistence.buscarPorId(ID)).thenReturn(Optional.of(PedidoFixture.pedidoSalvo()));
        when(produtoPersistence.buscarPorId(PRODUTO_ID)).thenReturn(Optional.of(produto));
        when(pedidoPersistence.atualizar(eq(ID), any(Pedido.class))).thenAnswer(inv -> inv.getArgument(1));

        ItemPedido solicitado = new ItemPedido(PRODUTO_ID, null, 5, null, null);
        RetornoPedido retorno = useCase.atualizar(ID, alteracao(null, List.of(solicitado)));

        assertThat(retorno.observacao()).isEqualTo(PedidoFixture.OBSERVACAO);
        assertThat(retorno.itens()).containsExactly(ItemPedido.criar(produto, 5));
        assertThat(retorno.valorTotal()).isEqualByComparingTo(new BigDecimal("99.50"));
    }

    @Test
    void deveLancarQuandoProdutoNaoEncontrado() {
        when(pedidoPersistence.buscarPorId(ID)).thenReturn(Optional.of(PedidoFixture.pedidoSalvo()));
        when(produtoPersistence.buscarPorId(PRODUTO_ID)).thenReturn(Optional.empty());

        Pedido alteracao = alteracao(null, List.of(PedidoFixture.itemSolicitado()));
        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Produto " + PRODUTO_ID + " não encontrado");
        verify(pedidoPersistence, never()).atualizar(anyString(), any());
    }

    @Test
    void deveLancarQuandoPedidoNaoEncontrado() {
        when(pedidoPersistence.buscarPorId(ID)).thenReturn(Optional.empty());

        Pedido alteracao = alteracao("nova", List.of());
        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Pedido " + ID + " não encontrado");
        verify(logger).warn(AtualizarPedidoUseCase.class, "Pedido " + ID + " não encontrado");
        verify(pedidoPersistence, never()).atualizar(anyString(), any());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void deveRecusarIdInvalido(String id) {
        Pedido alteracao = alteracao("nova", List.of());

        assertThatThrownBy(() -> useCase.atualizar(id, alteracao))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O ID do pedido é obrigatório");
        verifyNoInteractions(pedidoPersistence, produtoPersistence);
    }

    @Test
    void deveRecusarPedidoNulo() {
        assertThatThrownBy(() -> useCase.atualizar(ID, null))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("Pedido não pode ser nulo");
        verifyNoInteractions(pedidoPersistence, produtoPersistence);
    }

    @Test
    void deveRecusarAlteracaoSemDados() {
        Pedido alteracao = alteracao(null, null);

        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("Nenhum dado informado para atualização");
        verifyNoInteractions(pedidoPersistence, produtoPersistence);
    }

    private static Pedido alteracao(String observacao, List<ItemPedido> itens) {
        return new Pedido(null, null, null, null, null, observacao, itens, null, null);
    }
}
