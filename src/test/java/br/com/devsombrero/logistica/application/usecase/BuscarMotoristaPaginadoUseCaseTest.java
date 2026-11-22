package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.motorista.BuscarMotoristaPaginadoUseCase;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.support.MotoristaFixture;
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
class BuscarMotoristaPaginadoUseCaseTest {

    @Mock
    private MotoristaPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarMotoristaPaginadoUseCase useCase;

    @Test
    void deveRetornarPaginaMapeada() {
        Paginado<Motorista> pagina = new Paginado<>(List.of(MotoristaFixture.motoristaSalvo()), 11, 22, 33, 44L);
        when(persistence.buscarPaginado(10, 0)).thenReturn(pagina);

        Paginado<RetornoMotorista> resultado = useCase.buscarPaginado(0, 10);

        assertThat(resultado.conteudo()).containsExactly(MotoristaFixture.retornoMotorista());
        assertThat(resultado.paginaAtual()).isEqualTo(11);
        assertThat(resultado.tamanhoPagina()).isEqualTo(22);
        assertThat(resultado.totalPaginas()).isEqualTo(33);
        assertThat(resultado.totalElementos()).isEqualTo(44L);
        verify(logger).info(BuscarMotoristaPaginadoUseCase.class, "Busca paginada de motoristas realizada com sucesso");
    }

    @ParameterizedTest
    @CsvSource({"0,0", "0,-1", "-1,10"})
    void deveRecusarParametrosInvalidos(int paginaAtual, int tamanhoPagina) {
        assertThatThrownBy(() -> useCase.buscarPaginado(paginaAtual, tamanhoPagina))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O tamanho da página deve ser maior que zero e a página não pode ser negativa.");

        verifyNoInteractions(persistence);
        verify(logger).warn(BuscarMotoristaPaginadoUseCase.class, "Parâmetros inválidos para busca paginada do motorista");
    }
}
