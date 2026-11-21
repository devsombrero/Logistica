package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.produto.ProdutoDocument;
import br.com.devsombrero.logistica.domain.model.Produto;
import br.com.devsombrero.logistica.support.ProdutoFixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProdutoDocumentTest {

    @Test
    void fromDomainEToDomainDevemSerSimetricos() {
        Produto produto = ProdutoFixture.produtoSalvo();

        ProdutoDocument document = ProdutoDocument.fromDomain(produto);

        assertThat(document.id()).isEqualTo(produto.id());
        assertThat(document.dataCriacao()).isEqualTo(produto.dataCriacao());
        assertThat(ProdutoDocument.toDomain(document)).isEqualTo(produto);
    }

    @Test
    void atualizarDeveSubstituirApenasValoresPreenchidos() {
        ProdutoDocument original = ProdutoDocument.fromDomain(ProdutoFixture.produtoSalvo());
        Produto alteracao = new Produto(
                "SKU-002",
                null,
                "  ",
                new BigDecimal("2.00"),
                null,
                new BigDecimal("50.00"),
                null
        );

        ProdutoDocument atualizado = original.atualizar(alteracao);

        assertThat(atualizado.id()).isEqualTo(original.id());
        assertThat(atualizado.sku()).isEqualTo("SKU-002");
        assertThat(atualizado.nome()).isEqualTo(original.nome());
        assertThat(atualizado.descricao()).isEqualTo(original.descricao());
        assertThat(atualizado.peso()).isEqualTo(new BigDecimal("2.00"));
        assertThat(atualizado.altura()).isEqualTo(original.altura());
        assertThat(atualizado.comprimento()).isEqualTo(new BigDecimal("50.00"));
        assertThat(atualizado.valor()).isEqualTo(original.valor());
        assertThat(atualizado.dataCriacao()).isEqualTo(original.dataCriacao());
        assertThat(atualizado.dataAtualizacao()).isEqualTo(original.dataAtualizacao());
    }
}
