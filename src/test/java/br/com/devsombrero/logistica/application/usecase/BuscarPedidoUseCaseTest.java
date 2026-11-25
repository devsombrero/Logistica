package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.pedido.BuscarPedidoUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.support.PedidoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarPedidoUseCaseTest {

    private static final String ID = PedidoFixture.ID;

    @Mock
    private PedidoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarPedidoUseCase useCase;

    @Test
    void deveRetornarPedidoEncontrado() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(PedidoFixture.pedidoSalvo()));

        assertThat(useCase.buscarPorId(ID)).isEqualTo(PedidoFixture.retornoPedido());
        verify(logger).info(BuscarPedidoUseCase.class, "Buscando pedido " + ID);
    }

    @Test
    void deveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId(ID))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Pedido " + ID + " não encontrado");
        verify(logger).warn(BuscarPedidoUseCase.class, "Pedido " + ID + " não encontrado");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void deveRecusarIdInvalido(String id) {
        assertThatThrownBy(() -> useCase.buscarPorId(id))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O ID do pedido é obrigatório");
        verifyNoInteractions(persistence);
    }
}
