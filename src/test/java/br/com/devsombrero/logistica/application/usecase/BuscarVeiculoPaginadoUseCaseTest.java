package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.veiculo.BuscarVeiculoPaginadoUseCase;
import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import br.com.devsombrero.logistica.support.VeiculoFixture;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarVeiculoPaginadoUseCaseTest {

    @Mock
    private VeiculoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarVeiculoPaginadoUseCase useCase;

    private final Paginado<Veiculo> pagina =
            new Paginado<>(List.of(VeiculoFixture.veiculoSalvo()), 11, 22, 33, 44L);

    @Test
    void semFiltroDeveUsarBuscaPaginadaSimples() {
        when(persistence.buscarPaginado(10, 0)).thenReturn(pagina);

        Paginado<RetornoVeiculo> resultado = useCase.buscarPaginado(0, 10, null);

        assertResultadoMapeado(resultado);
        verify(persistence, never()).buscarPaginadoComFiltros(anyInt(), anyInt(), any());
        verify(logger).info(BuscarVeiculoPaginadoUseCase.class, "Busca paginada de veículos realizada com sucesso");
    }

    @Test
    void comFiltroDeveUsarBuscaComFiltros() {
        FiltroPesquisaVeiculo filtro = new FiltroPesquisaVeiculo("FH", null, null, StatusVeiculo.DISPONIVEL);
        when(persistence.buscarPaginadoComFiltros(0, 10, filtro)).thenReturn(pagina);

        Paginado<RetornoVeiculo> resultado = useCase.buscarPaginado(0, 10, filtro);

        assertResultadoMapeado(resultado);
        verify(persistence, never()).buscarPaginado(anyInt(), anyInt());
    }

    @ParameterizedTest
    @CsvSource({"0,0", "0,-1", "-1,10"})
    void deveRecusarParametrosInvalidos(int paginaAtual, int tamanhoPagina) {
        assertThatThrownBy(() -> useCase.buscarPaginado(paginaAtual, tamanhoPagina, null))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O tamanho da página deve ser maior que zero e a página não pode ser negativa.");

        verifyNoInteractions(persistence);
        verify(logger).warn(BuscarVeiculoPaginadoUseCase.class, "Parâmetros inválidos para busca paginada de veículos");
    }

    private void assertResultadoMapeado(Paginado<RetornoVeiculo> resultado) {
        assertThat(resultado.conteudo()).containsExactly(VeiculoFixture.retornoVeiculo());
        assertThat(resultado.paginaAtual()).isEqualTo(11);
        assertThat(resultado.tamanhoPagina()).isEqualTo(22);
        assertThat(resultado.totalPaginas()).isEqualTo(33);
        assertThat(resultado.totalElementos()).isEqualTo(44L);
    }
}
