package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.veiculo.ApagarVeiculoUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.support.VeiculoFixture;
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
class ApagarVeiculoUseCaseTest {

    private static final String ID = VeiculoFixture.ID;

    @Mock
    private VeiculoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private ApagarVeiculoUseCase useCase;

    @Test
    void deveApagarVeiculo() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(VeiculoFixture.veiculoSalvo()));

        useCase.apagar(ID);

        verify(persistence).deletar(ID);
        verify(logger).info(ApagarVeiculoUseCase.class, "Exclusão sucedida do veiculo");
    }

    @Test
    void deveLancarQuandoVeiculoNaoEncontrado() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.apagar(ID))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Veiculo não encontrado");

        verify(persistence, never()).deletar(anyString());
        verify(logger).warn(ApagarVeiculoUseCase.class, "Veiculo não encontrado com esse ID");
    }
}
