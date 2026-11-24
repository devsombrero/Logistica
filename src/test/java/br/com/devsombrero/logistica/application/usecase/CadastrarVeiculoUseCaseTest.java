package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.veiculo.CadastrarVeiculoUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import br.com.devsombrero.logistica.support.VeiculoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarVeiculoUseCaseTest {

    @Mock
    private VeiculoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private CadastrarVeiculoUseCase useCase;

    private final Veiculo novo = VeiculoFixture.novoVeiculo();

    @Test
    void deveCadastrarVeiculo() {
        when(persistence.existePorPlaca(VeiculoFixture.PLACA)).thenReturn(false);
        when(persistence.salvar(novo)).thenReturn(VeiculoFixture.veiculoSalvo());

        assertThat(useCase.cadastrar(novo)).isEqualTo(VeiculoFixture.retornoVeiculo());
        verify(logger).info(CadastrarVeiculoUseCase.class, "Cadastro de veiculo realizado com sucesso");
    }

    @Test
    void deveRecusarVeiculoNulo() {
        assertThatThrownBy(() -> useCase.cadastrar(null))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O veículo não pode ser nulo.");

        verifyNoInteractions(persistence);
        verify(logger).warn(CadastrarVeiculoUseCase.class,
                "Cadastro de veiculo recusado: O veiculo não pode ser 'null'");
    }

    @Test
    void deveRecusarPlacaDuplicada() {
        when(persistence.existePorPlaca(VeiculoFixture.PLACA)).thenReturn(true);

        assertThatThrownBy(() -> useCase.cadastrar(novo))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("Já existe um veículo cadastrado com essa placa.");

        verify(persistence, never()).salvar(any());
        verify(logger).warn(CadastrarVeiculoUseCase.class,
                "Cadastro de veiculo recusado: Veículo já tem cadastro com a placa informada");
    }
}
