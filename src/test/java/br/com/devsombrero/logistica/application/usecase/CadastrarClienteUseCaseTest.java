package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.usecase.cliente.CadastrarClienteUseCase;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.support.ClienteFixture;
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
class CadastrarClienteUseCaseTest {

    @Mock
    private ClientePersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private CadastrarClienteUseCase useCase;

    @Test
    void deveCadastrarCliente() {
        Cliente novo = ClienteFixture.novoCliente();
        when(persistence.existePorEmailOuDocumento(novo.email(), novo.documento())).thenReturn(false);
        when(persistence.salvar(novo)).thenReturn(ClienteFixture.clienteSalvo());

        RetornoCliente retorno = useCase.cadastrar(novo);

        assertThat(retorno).isEqualTo(ClienteFixture.retornoCliente());
        verify(logger).info(CadastrarClienteUseCase.class, "Cliente cadastrado com sucesso");
    }

    @Test
    void deveRecusarClienteNulo() {
        assertThatThrownBy(() -> useCase.cadastrar(null))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("Cliente não pode ser nulo");

        verifyNoInteractions(persistence);
        verify(logger).warn(CadastrarClienteUseCase.class, "Cadastro de cliente recusado: requisição nula");
    }

    @Test
    void deveRecusarClienteDuplicado() {
        Cliente novo = ClienteFixture.novoCliente();
        when(persistence.existePorEmailOuDocumento(novo.email(), novo.documento())).thenReturn(true);

        assertThatThrownBy(() -> useCase.cadastrar(novo))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("Cliente já existe com esse e-mail/documento");

        verify(persistence, never()).salvar(any());
        verify(logger).warn(CadastrarClienteUseCase.class,
                "Cadastro de cliente recusado: e-mail ou documento já cadastrado");
    }
}
