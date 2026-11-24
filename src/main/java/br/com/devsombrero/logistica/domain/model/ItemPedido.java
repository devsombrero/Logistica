package br.com.devsombrero.logistica.domain.model;

import java.math.BigDecimal;

public record ItemPedido(
        String produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal subTotal
) {
    public BigDecimal calcularSubTotal() {
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
