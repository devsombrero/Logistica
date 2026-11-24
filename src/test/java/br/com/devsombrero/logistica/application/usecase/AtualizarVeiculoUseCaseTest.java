package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.veiculo.AtualizarVeiculoUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import br.com.devsombrero.logistica.support.VeiculoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarVeiculoUseCaseTest {

    private static final String ID = VeiculoFixture.ID;

    @Mock
    private VeiculoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private AtualizarVeiculoUseCase useCase;

    private final Veiculo alteracao = VeiculoFixture.novoVeiculo();

    @Test
    void deveAtualizarVeiculo() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(VeiculoFixture.veiculoSalvo()));
        when(persistence.existePorPlacaIdDiferente(alteracao.placa(), ID)).thenReturn(false);
        when(persistence.atualizar(ID, alteracao)).thenReturn(VeiculoFixture.veiculoSalvo());

        assertThat(useCase.atualizar(ID, alteracao)).isEqualTo(VeiculoFixture.retornoVeiculo());
        verify(logger).info(AtualizarVeiculoUseCase.class, "Veiculo atualizado com sucesso");
    }

    @Test
    void deveLancarQuandoVeiculoNaoEncontrado() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Veiculo não encontrado");

        verify(persistence, never()).atualizar(anyString(), any());
        verify(logger).warn(AtualizarVeiculoUseCase.class, "Veiculo não encontrado para atualização");
    }

    @Test
    void deveRecusarPlacaDeOutroVeiculo() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(VeiculoFixture.veiculoSalvo()));
        when(persistence.existePorPlacaIdDiferente(alteracao.placa(), ID)).thenReturn(true);

        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("Já existe um veiculo cadastrado com essa placa");

        verify(persistence, never()).atualizar(anyString(), any());
        verify(logger).warn(AtualizarVeiculoUseCase.class, "Já existe um outro veiculo cadastrado com essa placa!");
    }
}
