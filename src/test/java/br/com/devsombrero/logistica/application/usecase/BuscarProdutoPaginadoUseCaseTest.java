package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.produto.BuscarProdutoPaginadoUseCase;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Produto;
import br.com.devsombrero.logistica.support.ProdutoFixture;
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
class BuscarProdutoPaginadoUseCaseTest {

    @Mock
    private ProdutoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarProdutoPaginadoUseCase useCase;

    @Test
    void deveRetornarPaginaMapeada() {
        Paginado<Produto> pagina = new Paginado<>(List.of(ProdutoFixture.produtoSalvo()), 11, 22, 33, 44L);
        when(persistence.buscarPaginado(10, 0)).thenReturn(pagina);

        Paginado<RetornoProduto> resultado = useCase.buscarPaginado(10, 0);

        assertThat(resultado.conteudo()).containsExactly(ProdutoFixture.retornoProduto());
        assertThat(resultado.paginaAtual()).isEqualTo(11);
        assertThat(resultado.tamanhoPagina()).isEqualTo(22);
        assertThat(resultado.totalPaginas()).isEqualTo(33);
        assertThat(resultado.totalElementos()).isEqualTo(44L);
        verify(logger).info(BuscarProdutoPaginadoUseCase.class, "Busca paginada de produtos realizada com sucesso");
    }

    @ParameterizedTest
    @CsvSource({"0,0", "-1,0", "10,-1"})
    void deveRecusarParametrosInvalidos(int tamanhoPagina, int paginaAtual) {
        assertThatThrownBy(() -> useCase.buscarPaginado(tamanhoPagina, paginaAtual))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O tamanho da página deve ser maior que zero e a página não pode ser negativa.");

        verifyNoInteractions(persistence);
        verify(logger).warn(BuscarProdutoPaginadoUseCase.class, "Parâmetros inválidos para busca paginada");
    }
}
