package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.produto.AtualizarProdutoUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.model.Produto;
import br.com.devsombrero.logistica.support.ProdutoFixture;
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
class AtualizarProdutoUseCaseTest {

    private static final String ID = ProdutoFixture.ID;

    @Mock
    private ProdutoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private AtualizarProdutoUseCase useCase;

    private final Produto alteracao = ProdutoFixture.novoProduto();

    @Test
    void deveAtualizarProduto() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(ProdutoFixture.produtoSalvo()));
        when(persistence.existePorSkuEIdDiferente(alteracao.sku(), ID)).thenReturn(false);
        when(persistence.atualizar(ID, alteracao)).thenReturn(ProdutoFixture.produtoSalvo());

        assertThat(useCase.atualizar(ID, alteracao)).isEqualTo(ProdutoFixture.retornoProduto());
        verify(logger).info(AtualizarProdutoUseCase.class, "Produto atualizado com sucesso");
    }

    @Test
    void deveLancarQuandoProdutoNaoExiste() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Produto não encontrado: " + ID);

        verify(persistence, never()).atualizar(anyString(), any());
        verify(logger).warn(AtualizarProdutoUseCase.class, "Produto não encontrado para atualização");
    }

    @Test
    void deveRecusarSkuDeOutroProduto() {
        when(persistence.buscarPorId(ID)).thenReturn(Optional.of(ProdutoFixture.produtoSalvo()));
        when(persistence.existePorSkuEIdDiferente(alteracao.sku(), ID)).thenReturn(true);

        assertThatThrownBy(() -> useCase.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("Já existe outro produto cadastrado com o SKU: " + alteracao.sku());

        verify(persistence, never()).atualizar(anyString(), any());
        verify(logger).warn(AtualizarProdutoUseCase.class, "Já existe outro produto cadastrado com este SKU");
    }
}
