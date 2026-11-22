package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.motorista.CadastrarMotoristaUseCase;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarMotoristaUseCaseTest {

    private static final String MENSAGEM_CNH_INVALIDA =
            "Não é possível cadastrar um motorista com a CNH vencida ou sem validade!";
    private static final String LOG_CNH_INVALIDA =
            "Cadastro de motorista recusado: CNH vencida ou sem validade!";

    @Mock
    private MotoristaPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private CadastrarMotoristaUseCase useCase;

    @Test
    void deveCadastrarMotorista() {
        Motorista novo = MotoristaFixture.novoMotorista();
        when(persistence.existePorCpf(novo.cpf())).thenReturn(false);
        when(persistence.existePorCnh(novo.cnh())).thenReturn(false);
        when(persistence.salvar(novo)).thenReturn(MotoristaFixture.motoristaSalvo());

        assertThat(useCase.cadastrar(novo)).isEqualTo(MotoristaFixture.retornoMotorista());
        verify(logger).info(CadastrarMotoristaUseCase.class, "Motorista cadastrado com sucesso");
    }

    @Test
    void deveRecusarMotoristaNulo() {
        assertThatThrownBy(() -> useCase.cadastrar(null))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O motorista não pode ser nulo!");

        verifyNoInteractions(persistence);
        verify(logger).warn(CadastrarMotoristaUseCase.class,
                "Cadastro de motorista recusado: Motorista não pode ser null!");
    }

    @Test
    void deveRecusarCpfDuplicado() {
        Motorista novo = MotoristaFixture.novoMotorista();
        when(persistence.existePorCpf(novo.cpf())).thenReturn(true);

        assertThatThrownBy(() -> useCase.cadastrar(novo))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("O motorista com esse cpf já existe na base de dados!");

        verify(persistence, never()).salvar(any());
        verify(logger).warn(CadastrarMotoristaUseCase.class,
                "Cadastro de motorista recusado: Motorista com esse CPF já existe!");
    }

    @Test
    void deveRecusarCnhDuplicada() {
        Motorista novo = MotoristaFixture.novoMotorista();
        when(persistence.existePorCpf(novo.cpf())).thenReturn(false);
        when(persistence.existePorCnh(novo.cnh())).thenReturn(true);

        assertThatThrownBy(() -> useCase.cadastrar(novo))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("O motorista com esse CNH já existe na base de dados!");

        verify(persistence, never()).salvar(any());
        verify(logger).warn(CadastrarMotoristaUseCase.class,
                "Cadastro de motorista recusado: Motorista com esse CNH já existe!");
    }

    @Test
    void deveRecusarCnhSemValidade() {
        Motorista novo = MotoristaFixture.novoMotorista(null);
        when(persistence.existePorCpf(novo.cpf())).thenReturn(false);
        when(persistence.existePorCnh(novo.cnh())).thenReturn(false);

        assertThatThrownBy(() -> useCase.cadastrar(novo))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage(MENSAGEM_CNH_INVALIDA);

        verify(persistence, never()).salvar(any());
        verify(logger).warn(CadastrarMotoristaUseCase.class, LOG_CNH_INVALIDA);
    }

    @Test
    void deveRecusarCnhVencida() {
        Motorista novo = MotoristaFixture.novoMotorista(LocalDate.now().minusDays(1));
        when(persistence.existePorCpf(novo.cpf())).thenReturn(false);
        when(persistence.existePorCnh(novo.cnh())).thenReturn(false);

        assertThatThrownBy(() -> useCase.cadastrar(novo))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage(MENSAGEM_CNH_INVALIDA);

        verify(persistence, never()).salvar(any());
        verify(logger).warn(CadastrarMotoristaUseCase.class, LOG_CNH_INVALIDA);
    }
}
