package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.usecase.cliente.ApagarClienteUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.support.ClienteFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApagarClienteUseCaseTest {

    @Mock
    private ClientePersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private ApagarClienteUseCase useCase;

    @Test
    void deveApagarCliente() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));

        useCase.apagar("1");

        verify(persistence).deletar("1");
        verify(logger).info(ApagarClienteUseCase.class, "Cliente excluído com sucesso");
    }

    @Test
    void deveLancarQuandoClienteNaoExiste() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.apagar("1"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Cliente não encontrado com esse ID");

        verify(persistence, never()).deletar(anyString());
        verify(logger).warn(ApagarClienteUseCase.class, "Cliente não encontrado para exclusão");
    }
}
