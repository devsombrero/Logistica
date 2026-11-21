package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.produto.BuscarProdutoUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.support.ProdutoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarProdutoUseCaseTest {

    @Mock
    private ProdutoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private BuscarProdutoUseCase useCase;

    @Test
    void buscarPorIdDeveRetornarProduto() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.of(ProdutoFixture.produtoSalvo()));

        assertThat(useCase.buscarPorId("1")).isEqualTo(ProdutoFixture.retornoProduto());
        verify(logger).info(BuscarProdutoUseCase.class, "Produto encontrado com esse ID com sucesso");
    }

    @Test
    void buscarPorIdDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId("1"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Produto não encontrado com esse ID");
        verify(logger).warn(BuscarProdutoUseCase.class, "Produto não encontrado com esse ID");
    }

    @Test
    void buscarPorSkuDeveRetornarProduto() {
        when(persistence.buscarPorSku("s")).thenReturn(Optional.of(ProdutoFixture.produtoSalvo()));

        assertThat(useCase.buscarPorSku("s")).isEqualTo(ProdutoFixture.retornoProduto());
        verify(logger).info(BuscarProdutoUseCase.class, "Produto encontrado com esse SKU com sucesso");
    }

    @Test
    void buscarPorSkuDeveLancarQuandoNaoEncontrado() {
        when(persistence.buscarPorSku("s")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorSku("s"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Produto não encontrado com esse SKU");
        verify(logger).warn(BuscarProdutoUseCase.class, "Produto não encontrado com esse SKU");
    }
}
