package br.com.devsombrero.logistica.domain.dto;

import java.math.BigDecimal;

public record RetornoProduto(
        String id,
        String sku,
        String nome,
        String descricao,
        BigDecimal peso,
        BigDecimal altura,
        BigDecimal comprimento,
        BigDecimal valor
) {
}
