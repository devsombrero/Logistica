package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.usecase.cliente.BuscarClienteUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.support.ClienteFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarClienteUseCaseTest {

    @Mock
    private ClientePersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarClienteUseCase useCase;

    @Test
    void buscarPorIdDeveRetornarCliente() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));

        assertThat(useCase.buscarPorId("1")).isEqualTo(ClienteFixture.retornoCliente());
        verify(logger).info(BuscarClienteUseCase.class, "Cliente encontrado com sucesso por ID");
    }

    @Test
    void buscarPorIdDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId("1"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Cliente não encontrado com esse ID");
        verify(logger).warn(BuscarClienteUseCase.class, "Cliente não encontrado na busca por ID");
    }

    @Test
    void buscarPorEmailDeveRetornarCliente() {
        when(persistence.buscarPorEmail("e")).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));

        assertThat(useCase.buscarPorEmail("e")).isEqualTo(ClienteFixture.retornoCliente());
        verify(logger).info(BuscarClienteUseCase.class, "Cliente encontrado com sucesso por e-mail");
    }

    @Test
    void buscarPorEmailDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorEmail("e")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorEmail("e"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Cliente não encontrado com esse e-mail");
        verify(logger).warn(BuscarClienteUseCase.class, "Cliente não encontrado na busca por e-mail");
    }

    @Test
    void buscarPorDocumentoDeveRetornarCliente() {
        when(persistence.buscarPorDocumento("d")).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));

        assertThat(useCase.buscarPorDocumento("d")).isEqualTo(ClienteFixture.retornoCliente());
        verify(logger).info(BuscarClienteUseCase.class, "Cliente encontrado com sucesso por documento");
    }

    @Test
    void buscarPorDocumentoDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorDocumento("d")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorDocumento("d"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Cliente não encontrado com esse documento");
        verify(logger).warn(BuscarClienteUseCase.class, "Cliente não encontrado na busca por documento");
    }
}
