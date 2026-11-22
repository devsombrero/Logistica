package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.motorista.BuscarMotoristaUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.support.MotoristaFixture;
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
class BuscarMotoristaUseCaseTest {

    @Mock
    private MotoristaPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarMotoristaUseCase useCase;

    @Test
    void buscarPorIdDeveRetornarMotorista() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.of(MotoristaFixture.motoristaSalvo()));

        assertThat(useCase.buscarPorId("1")).isEqualTo(MotoristaFixture.retornoMotorista());
        verify(logger).info(BuscarMotoristaUseCase.class, "Iniciando busca do motorista por id");
    }

    @Test
    void buscarPorIdDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId("1"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Motorista não encontrado!");
        verify(logger).warn(BuscarMotoristaUseCase.class, "Motorista não encontrado com esse id");
    }

    @Test
    void buscarPorCnhDeveRetornarMotorista() {
        when(persistence.buscarPorCnh("c")).thenReturn(Optional.of(MotoristaFixture.motoristaSalvo()));

        assertThat(useCase.buscarPorCnh("c")).isEqualTo(MotoristaFixture.retornoMotorista());
        verify(logger).info(BuscarMotoristaUseCase.class, "Iniciando busca do motorista por cnh");
    }

    @Test
    void buscarPorCnhDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorCnh("c")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorCnh("c"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Motorista não encontrado!");
        verify(logger).warn(BuscarMotoristaUseCase.class, "Motorista não encontrado com esse cnh");
    }

    @Test
    void buscarPorCpfDeveRetornarMotorista() {
        when(persistence.buscarPorCpf("p")).thenReturn(Optional.of(MotoristaFixture.motoristaSalvo()));

        assertThat(useCase.buscarPorCpf("p")).isEqualTo(MotoristaFixture.retornoMotorista());
        verify(logger).info(BuscarMotoristaUseCase.class, "Iniciando busca do motorista por cpf");
    }

    @Test
    void buscarPorCpfDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorCpf("p")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorCpf("p"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Motorista não encontrado!");
        verify(logger).warn(BuscarMotoristaUseCase.class, "Motorista não encontrado com esse cpf");
    }
}
