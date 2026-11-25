package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.pedido.BuscarPedidoPaginadoUseCase;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Pedido;
import br.com.devsombrero.logistica.support.ClienteFixture;
import br.com.devsombrero.logistica.support.PedidoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarPedidoPaginadoUseCaseTest {

    private static final String CLIENTE_ID = PedidoFixture.CLIENTE_ID;

    @Mock
    private PedidoPersistenceOutputPort pedidoPersistence;
    @Mock
    private ClientePersistenceOutputPort clientePersistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarPedidoPaginadoUseCase useCase;

    private final Paginado<Pedido> pagina =
            new Paginado<>(List.of(PedidoFixture.pedidoSalvo()), 1, 50, 3, 101L);

    @Test
    void buscarPaginadoDeveConverterPagina() {
        when(pedidoPersistence.buscarPaginado(50, 1)).thenReturn(pagina);

        Paginado<RetornoPedido> resultado = useCase.buscarPaginado(50, 1);

        assertPaginaConvertida(resultado);
        verify(logger).info(BuscarPedidoPaginadoUseCase.class, "Buscando pedidos - página 1, tamanho 50");
    }

    @ParameterizedTest
    @CsvSource({"10,-1,A página atual não pode ser negativa",
            "0,0,O tamanho da página deve estar entre 1 e 50",
            "51,0,O tamanho da página deve estar entre 1 e 50"})
    void buscarPaginadoDeveRecusarPaginacaoInvalida(int tamanhoPagina, int paginaAtual, String mensagem) {
        assertThatThrownBy(() -> useCase.buscarPaginado(tamanhoPagina, paginaAtual))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage(mensagem);
        verifyNoInteractions(pedidoPersistence);
    }

    @Test
    void buscarPorClienteDeveConverterPagina() {
        when(clientePersistence.buscarPorId(CLIENTE_ID)).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));
        when(pedidoPersistence.buscarPedidosPorCliente(CLIENTE_ID, 50, 1)).thenReturn(pagina);

        assertPaginaConvertida(useCase.buscarPorCliente(CLIENTE_ID, 50, 1));
        verify(logger).info(BuscarPedidoPaginadoUseCase.class, "Buscando pedidos do cliente " + CLIENTE_ID);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void buscarPorClienteDeveRecusarClienteInvalido(String clienteId) {
        assertThatThrownBy(() -> useCase.buscarPorCliente(clienteId, 10, 0))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O ID do cliente é obrigatório");
        verifyNoInteractions(clientePersistence, pedidoPersistence);
    }

    @Test
    void buscarPorClienteDeveValidarPaginacao() {
        assertThatThrownBy(() -> useCase.buscarPorCliente(CLIENTE_ID, 10, -1))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("A página atual não pode ser negativa");
        verifyNoInteractions(clientePersistence, pedidoPersistence);
    }

    @Test
    void buscarPorClienteDeveLancarQuandoClienteNaoEncontrado() {
        when(clientePersistence.buscarPorId(CLIENTE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorCliente(CLIENTE_ID, 10, 0))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Cliente " + CLIENTE_ID + " não encontrado");
        verify(logger).warn(BuscarPedidoPaginadoUseCase.class, "Cliente " + CLIENTE_ID + " não encontrado");
        verifyNoInteractions(pedidoPersistence);
    }

    @Test
    void buscarPorStatusDeveConverterPagina() {
        when(pedidoPersistence.buscarPedidoPorStatus(StatusPedido.PENDENTE, 50, 1)).thenReturn(pagina);

        assertPaginaConvertida(useCase.buscarPorStatus(StatusPedido.PENDENTE, 50, 1));
        verify(logger).info(BuscarPedidoPaginadoUseCase.class, "Buscando pedidos com status PENDENTE");
    }

    @Test
    void buscarPorStatusDeveRecusarStatusNulo() {
        assertThatThrownBy(() -> useCase.buscarPorStatus(null, 10, 0))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O status do pedido é obrigatório");
        verifyNoInteractions(pedidoPersistence);
    }

    @Test
    void buscarPorStatusDeveValidarPaginacao() {
        assertThatThrownBy(() -> useCase.buscarPorStatus(StatusPedido.PENDENTE, 0, 0))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O tamanho da página deve estar entre 1 e 50");
        verifyNoInteractions(pedidoPersistence);
    }

    private static void assertPaginaConvertida(Paginado<RetornoPedido> resultado) {
        assertThat(resultado.conteudo()).containsExactly(PedidoFixture.retornoPedido());
        assertThat(resultado.paginaAtual()).isEqualTo(1);
        assertThat(resultado.tamanhoPagina()).isEqualTo(50);
        assertThat(resultado.totalPaginas()).isEqualTo(3);
        assertThat(resultado.totalElementos()).isEqualTo(101L);
    }
}
