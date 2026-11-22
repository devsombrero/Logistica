package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.motorista.ApagarMotoristaUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.support.MotoristaFixture;
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
class ApagarMotoristaUseCaseTest {

    @Mock
    private MotoristaPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private ApagarMotoristaUseCase useCase;

    @Test
    void deveApagarMotorista() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.of(MotoristaFixture.motoristaSalvo()));

        useCase.apagar("1");

        verify(persistence).deletar("1");
        verify(logger).info(ApagarMotoristaUseCase.class, "Motorista apagado com sucesso");
    }

    @Test
    void deveLancarQuandoMotoristaNaoExiste() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.apagar("1"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Motorista não encontrado com esse ID");

        verify(persistence, never()).deletar(anyString());
        verify(logger).warn(ApagarMotoristaUseCase.class, "Motorista não encontrado com esse ID");
    }
}
