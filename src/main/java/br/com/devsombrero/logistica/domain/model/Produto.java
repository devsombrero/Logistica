package br.com.devsombrero.logistica.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Produto(
        String id,
        String sku,
        String nome,
        String descricao,
        BigDecimal peso,
        BigDecimal altura,
        BigDecimal comprimento,
        BigDecimal valor,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {

    public Produto(
            String sku,
            String nome,
            String descricao,
            BigDecimal peso,
            BigDecimal altura,
            BigDecimal comprimento,
            BigDecimal valor
    ) {
        this(
                null,
                sku,
                nome,
                descricao,
                peso,
                altura,
                comprimento,
                valor,
                null,
                null
        );
    }
}
