package br.com.devsombrero.logistica.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProdutoTest {

    @Test
    void construtorSemIdDeveDeixarIdEDatasNulos() {
        Produto produto = new Produto("SKU", "Nome", "Desc",
                BigDecimal.ONE, BigDecimal.TWO, BigDecimal.TEN, BigDecimal.ONE);

        assertThat(produto.id()).isNull();
        assertThat(produto.dataCriacao()).isNull();
        assertThat(produto.dataAtualizacao()).isNull();
        assertThat(produto.sku()).isEqualTo("SKU");
        assertThat(produto.nome()).isEqualTo("Nome");
        assertThat(produto.descricao()).isEqualTo("Desc");
        assertThat(produto.peso()).isEqualTo(BigDecimal.ONE);
        assertThat(produto.altura()).isEqualTo(BigDecimal.TWO);
        assertThat(produto.comprimento()).isEqualTo(BigDecimal.TEN);
        assertThat(produto.valor()).isEqualTo(BigDecimal.ONE);
    }
}
