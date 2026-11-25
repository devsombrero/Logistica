package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.pedido.CancelarPedidoUseCase;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Pedido;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelarPedidoUseCaseTest {

    private static final String ID = PedidoFixture.ID;

    @Mock
    private PedidoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private CancelarPedidoUseCase useCase;

    @Test
    void deveCancelarPedidoEPersistir() {
        Pedido cancelado = PedidoFixture.pedido(StatusPedido.CANCELADO);
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(PedidoFixture.pedidoSalvo()));
        when(persistence.atualizar(ID, cancelado)).thenReturn(cancelado);

        RetornoPedido retorno = useCase.cancelar(ID);

        assertThat(retorno.statusPedido()).isEqualTo(StatusPedido.CANCELADO);
        verify(logger).info(CancelarPedidoUseCase.class, "Pedido " + ID + " cancelado com sucesso");
    }

    @Test
    void deveLancarQuandoPedidoNaoEncontrado() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.cancelar(ID))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Pedido " + ID + " não encontrado");
        verify(logger).warn(CancelarPedidoUseCase.class, "Pedido " + ID + " não encontrado");
        verify(persistence, never()).atualizar(anyString(), any());
    }

    @Test
    void devePropagarRegraDeDominio() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(PedidoFixture.pedido(StatusPedido.CANCELADO)));

        assertThatThrownBy(() -> useCase.cancelar(ID))
                .isInstanceOf(EntidadeUnprocessableException.class);
        verify(persistence, never()).atualizar(anyString(), any());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void deveRecusarIdInvalido(String id) {
        assertThatThrownBy(() -> useCase.cancelar(id))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O ID do pedido é obrigatório");
        verifyNoInteractions(persistence);
    }
}
