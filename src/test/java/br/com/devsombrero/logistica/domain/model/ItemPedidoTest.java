package br.com.devsombrero.logistica.domain.model;

import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.support.PedidoFixture;
import br.com.devsombrero.logistica.support.ProdutoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemPedidoTest {

    private static final String ID = ProdutoFixture.ID;

    @Test
    void criarDeveCalcularSubTotalComDadosDoProduto() {
        ItemPedido item = ItemPedido.criar(ProdutoFixture.produtoSalvo(), 2);

        assertThat(item).isEqualTo(PedidoFixture.itemSalvo());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {0, -1})
    void criarDeveRecusarQuantidadeInvalida(Integer quantidade) {
        Produto produto = ProdutoFixture.produtoSalvo();

        assertThatThrownBy(() -> ItemPedido.criar(produto, quantidade))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("Quantidade inválida para o produto " + ID);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "-0.01")
    void criarDeveRecusarProdutoComValorInvalido(String valor) {
        Produto produto = produtoComValor(valor == null ? null : new BigDecimal(valor));

        assertThatThrownBy(() -> ItemPedido.criar(produto, 1))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("Produto " + ID + " possui valor inválido");
    }

    @Test
    void criarDeveAceitarProdutoComValorZero() {
        ItemPedido item = ItemPedido.criar(produtoComValor(BigDecimal.ZERO), 3);

        assertThat(item.subTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void calcularSubTotalDeveMultiplicarValorPelaQuantidade() {
        ItemPedido item = new ItemPedido(ID, "Caixa", 3, new BigDecimal("10.00"), null);

        assertThat(item.calcularSubTotal()).isEqualByComparingTo("30.00");
    }

    private static Produto produtoComValor(BigDecimal valor) {
        Produto base = ProdutoFixture.produtoSalvo();
        return new Produto(base.id(), base.sku(), base.nome(), base.descricao(), base.peso(),
                base.altura(), base.comprimento(), valor, base.dataCriacao(), base.dataAtualizacao());
    }
}
