package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.produto.ProdutoDocument;
import br.com.devsombrero.logistica.adapter.out.persistence.produto.ProdutoMongoRepository;
import br.com.devsombrero.logistica.adapter.out.persistence.produto.ProdutoPersistenceAdapter;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Produto;
import br.com.devsombrero.logistica.support.ProdutoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoPersistenceAdapterTest {

    private static final String ID = ProdutoFixture.ID;
    private static final String SKU = ProdutoFixture.SKU;

    @Mock
    private ProdutoMongoRepository repository;

    @InjectMocks
    private ProdutoPersistenceAdapter adapter;

    private final ProdutoDocument document = ProdutoDocument.fromDomain(ProdutoFixture.produtoSalvo());

    @Test
    void buscarPorSkuDeveDelegar() {
        when(repository.findBySku(SKU)).thenReturn(Optional.of(ProdutoFixture.produtoSalvo()));

        assertThat(adapter.buscarPorSku(SKU)).contains(ProdutoFixture.produtoSalvo());
    }

    @Test
    void existePorSkuDeveDelegar() {
        when(repository.existsBySku(SKU)).thenReturn(true);

        assertThat(adapter.existePorSku(SKU)).isTrue();
    }

    @Test
    void existePorSkuEIdDiferenteDeveDelegar() {
        when(repository.existePorSkuEIdDiferente(SKU, ID)).thenReturn(true);

        assertThat(adapter.existePorSkuEIdDiferente(SKU, ID)).isTrue();
    }

    @Test
    void buscarPaginadoDeveConsultarComPaginacaoEConverter() {
        Pageable pageable = PageRequest.of(1, 2);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(document), pageable, 3));

        Paginado<Produto> resultado = adapter.buscarPaginado(2, 1);

        assertThat(resultado.conteudo()).containsExactly(ProdutoFixture.produtoSalvo());
        assertThat(resultado.paginaAtual()).isEqualTo(1);
        assertThat(resultado.tamanhoPagina()).isEqualTo(2);
        assertThat(resultado.totalPaginas()).isEqualTo(2);
        assertThat(resultado.totalElementos()).isEqualTo(3L);
    }

    @Test
    void buscarPorIdDeveConverterDocumento() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));

        assertThat(adapter.buscarPorId(ID)).contains(ProdutoFixture.produtoSalvo());
    }

    @Test
    void buscarPorIdDeveRetornarVazio() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThat(adapter.buscarPorId(ID)).isEmpty();
    }

    @Test
    void salvarDeveConverterEPersistir() {
        Produto novo = ProdutoFixture.novoProduto();
        when(repository.save(ProdutoDocument.fromDomain(novo))).thenReturn(document);

        assertThat(adapter.salvar(novo)).isEqualTo(ProdutoFixture.produtoSalvo());
    }

    @Test
    void atualizarDeveMesclarEPersistir() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));
        when(repository.save(any(ProdutoDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        Produto alteracao = new Produto(null, "Caixa grande", null, null, null, null, new BigDecimal("29.90"));
        Produto atualizado = adapter.atualizar(ID, alteracao);

        assertThat(atualizado.nome()).isEqualTo("Caixa grande");
        assertThat(atualizado.valor()).isEqualTo(new BigDecimal("29.90"));
        assertThat(atualizado.sku()).isEqualTo(SKU);
        ArgumentCaptor<ProdutoDocument> captor = ArgumentCaptor.forClass(ProdutoDocument.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(ID);
    }

    @Test
    void atualizarDeveLancarQuandoNaoEncontrado() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Produto alteracao = ProdutoFixture.novoProduto();
        assertThatThrownBy(() -> adapter.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Produto não encontrado: " + ID);
        verify(repository, never()).save(any());
    }

    @Test
    void deletarDeveRemoverQuandoExiste() {
        when(repository.existsById(ID)).thenReturn(true);

        adapter.deletar(ID);

        verify(repository).deleteById(ID);
    }

    @Test
    void deletarDeveLancarQuandoNaoExiste() {
        when(repository.existsById(ID)).thenReturn(false);

        assertThatThrownBy(() -> adapter.deletar(ID))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Produto não encontrado: " + ID);
        verify(repository, never()).deleteById(anyString());
    }
}
