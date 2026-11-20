package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.LoggerOutputPort;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.support.ClienteFixture;
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
class AtualizarClienteUseCaseTest {

    private static final String ID = ClienteFixture.ID;

    @Mock
    private ClientePersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private AtualizarClienteUseCase useCase;

    private final Cliente alteracao = ClienteFixture.novoCliente();

    @Test
    void deveAtualizarCliente() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));
        when(persistence.existeEmailOuDocumentoEmOutroCliente(alteracao.email(), alteracao.documento(), ID))
                .thenReturn(false);
        when(persistence.atualizar(ID, alteracao)).thenReturn(ClienteFixture.clienteSalvo());

        assertThat(useCase.atualizar(ID, alteracao)).isEqualTo(ClienteFixture.retornoCliente());
        verify(logger).info(AtualizarClienteUseCase.class, "Cliente atualizado com sucesso");
    }

    @Test
    void deveLancarQuandoClienteNaoExiste() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Cliente não encontrado: " + ID);

        verify(persistence, never()).atualizar(anyString(), any());
        verify(logger).warn(AtualizarClienteUseCase.class, "Cliente não encontrado para atualização");
    }

    @Test
    void deveRecusarEmailOuDocumentoDeOutroCliente() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(ClienteFixture.clienteSalvo()));
        when(persistence.existeEmailOuDocumentoEmOutroCliente(alteracao.email(), alteracao.documento(), ID))
                .thenReturn(true);

        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("O e-mail ou documento informado já pertence a outro cliente.");

        verify(persistence, never()).atualizar(anyString(), any());
        verify(logger).warn(AtualizarClienteUseCase.class,
                "Atualização de cliente recusada: e-mail ou documento já cadastrado");
    }
}
