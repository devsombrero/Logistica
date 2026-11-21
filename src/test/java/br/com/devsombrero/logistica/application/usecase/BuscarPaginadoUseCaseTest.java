package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.usecase.cliente.BuscarClientePaginadoUseCase;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.support.ClienteFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarPaginadoUseCaseTest {

    @Mock
    private ClientePersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarClientePaginadoUseCase useCase;

    @Test
    void deveRetornarPaginaMapeada() {
        Paginado<Cliente> pagina = new Paginado<>(List.of(ClienteFixture.clienteSalvo()), 11, 22, 33, 44L);
        when(persistence.buscarPaginado(10, 0)).thenReturn(pagina);

        Paginado<RetornoCliente> resultado = useCase.buscarPaginado(10, 0);

        assertThat(resultado.conteudo()).containsExactly(ClienteFixture.retornoCliente());
        assertThat(resultado.paginaAtual()).isEqualTo(11);
        assertThat(resultado.tamanhoPagina()).isEqualTo(22);
        assertThat(resultado.totalPaginas()).isEqualTo(33);
        assertThat(resultado.totalElementos()).isEqualTo(44L);
        verify(logger).info(BuscarClientePaginadoUseCase.class, "Busca paginada de clientes realizada com sucesso");
    }

    @ParameterizedTest
    @CsvSource({"0,0", "-1,0", "10,-1"})
    void deveRecusarParametrosInvalidos(int tamanhoPagina, int paginaAtual) {
        assertThatThrownBy(() -> useCase.buscarPaginado(tamanhoPagina, paginaAtual))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O tamanho da página deve ser maior que zero e a página não pode ser negativa.");

        verifyNoInteractions(persistence);
        verify(logger).warn(BuscarClientePaginadoUseCase.class, "Parâmetros inválidos para busca paginada");
    }
}
