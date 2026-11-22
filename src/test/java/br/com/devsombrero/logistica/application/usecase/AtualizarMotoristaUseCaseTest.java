package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.motorista.AtualizarMotoristaUseCase;
import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.support.MotoristaFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarMotoristaUseCaseTest {

    private static final String ID = MotoristaFixture.ID;

    @Mock
    private MotoristaPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private AtualizarMotoristaUseCase useCase;

    private final Motorista alteracao = MotoristaFixture.novoMotorista();

    @Test
    void deveAtualizarMotorista() {
        when(persistence.existeCnhOuCpfEmOutroMotorista(alteracao.cnh(), alteracao.cpf(), ID)).thenReturn(false);
        when(persistence.atualizar(ID, alteracao)).thenReturn(MotoristaFixture.motoristaSalvo());

        assertThat(useCase.atualizar(ID, alteracao)).isEqualTo(MotoristaFixture.retornoMotorista());
        verify(logger).info(AtualizarMotoristaUseCase.class, "Motorista atualizado com sucesso");
    }

    @Test
    void deveAtualizarMotoristaSemValidadeCnh() {
        Motorista semValidade = new Motorista(null, null, null, null, null, StatusMotorista.FERIAS);
        when(persistence.existeCnhOuCpfEmOutroMotorista(null, null, ID)).thenReturn(false);
        when(persistence.atualizar(ID, semValidade)).thenReturn(MotoristaFixture.motoristaSalvo());

        assertThat(useCase.atualizar(ID, semValidade)).isEqualTo(MotoristaFixture.retornoMotorista());
    }

    @Test
    void deveRecusarMotoristaNulo() {
        assertThatThrownBy(() -> useCase.atualizar(ID, null))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O motorista não pode ser nulo!");

        verifyNoInteractions(persistence);
        verify(logger).warn(AtualizarMotoristaUseCase.class,
                "Atualização de motorista recusado: Motorista não pode ser null!");
    }

    @Test
    void deveRecusarCnhOuCpfDeOutroMotorista() {
        when(persistence.existeCnhOuCpfEmOutroMotorista(alteracao.cnh(), alteracao.cpf(), ID)).thenReturn(true);

        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("Já existe outro motorista cadastrado com essa CNH ou CPF!");

        verify(persistence, never()).atualizar(anyString(), any());
        verify(logger).warn(AtualizarMotoristaUseCase.class,
                "Atualização de motorista recusado: Outro motorista já existe com esse CNH/CPF");
    }

    @Test
    void deveRecusarCnhVencida() {
        Motorista vencido = MotoristaFixture.novoMotorista(LocalDate.now().minusDays(1));
        when(persistence.existeCnhOuCpfEmOutroMotorista(vencido.cnh(), vencido.cpf(), ID)).thenReturn(false);

        assertThatThrownBy(() -> useCase.atualizar(ID, vencido))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("Não é possível cadastrar um motorista com a CNH vencida ou sem validade!");

        verify(persistence, never()).atualizar(anyString(), any());
        verify(logger).warn(AtualizarMotoristaUseCase.class,
                "Atualização de motorista recusado: CNH vencida ou sem validade!");
    }
}
