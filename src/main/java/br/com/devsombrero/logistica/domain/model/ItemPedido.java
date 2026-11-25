package br.com.devsombrero.logistica.domain.model;

import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;

import java.math.BigDecimal;

public record ItemPedido(
        String produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal subTotal
) {

    public static ItemPedido criar(Produto produto, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new RequisicaoInvalidaException(
                    "Quantidade inválida para o produto " + produto.id());
        }
        if (produto.valor() == null || produto.valor().signum() < 0) {
            throw new RequisicaoInvalidaException(
                    "Produto " + produto.id() + " possui valor inválido");
        }

        BigDecimal subTotal = produto.valor().multiply(BigDecimal.valueOf(quantidade));

        return new ItemPedido(
                produto.id(),
                produto.nome(),
                quantidade,
                produto.valor(),
                subTotal
        );
    }

    public BigDecimal calcularSubTotal() {
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}