package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.veiculo.BuscarVeiculoUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.support.VeiculoFixture;
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
class BuscarVeiculoUseCaseTest {

    private static final String ID = VeiculoFixture.ID;
    private static final String PLACA = VeiculoFixture.PLACA;

    @Mock
    private VeiculoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarVeiculoUseCase useCase;

    @Test
    void buscarPorIdDeveRetornarVeiculo() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(VeiculoFixture.veiculoSalvo()));

        assertThat(useCase.buscarPorId(ID)).isEqualTo(VeiculoFixture.retornoVeiculo());
        verify(logger).info(BuscarVeiculoUseCase.class, "Consulta de veiculo por ID realizada com sucesso");
    }

    @Test
    void buscarPorIdDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId(ID))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Veiculo não encontrado com o ID informado");
        verify(logger).info(BuscarVeiculoUseCase.class, "Consulta de veículo não encontrado com esse ID");
    }

    @Test
    void buscarPorPlacaDeveRetornarVeiculo() {
        when(persistence.buscarPorPlaca(PLACA)).thenReturn(Optional.of(VeiculoFixture.veiculoSalvo()));

        assertThat(useCase.buscarPorPlaca(PLACA)).isEqualTo(VeiculoFixture.retornoVeiculo());
        verify(logger).info(BuscarVeiculoUseCase.class, "Consulta de veiculo por placa realizada com sucesso");
    }

    @Test
    void buscarPorPlacaDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorPlaca(PLACA)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorPlaca(PLACA))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Veiculo não encontrado com o placa informada");
        verify(logger).info(BuscarVeiculoUseCase.class, "Consulta de veículo não encontrado com essa placa");
    }
}
