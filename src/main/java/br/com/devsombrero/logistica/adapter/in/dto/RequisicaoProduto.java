package br.com.devsombrero.logistica.adapter.in.dto;

import br.com.devsombrero.logistica.domain.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RequisicaoProduto(

        @NotBlank(message = "SKU é obrigatório")
        String sku,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Peso é obrigatório")
        @Positive(message = "Peso deve ser maior que zero")
        BigDecimal peso,

        @NotNull(message = "Altura é obrigatória")
        @Positive(message = "Altura deve ser maior que zero")
        BigDecimal altura,

        @NotNull(message = "Comprimento é obrigatório")
        @Positive(message = "Comprimento deve ser maior que zero")
        BigDecimal comprimento,

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser maior que zero")
        BigDecimal valor

) {
    public Produto toDomain() {
        return new Produto(
                sku,
                nome,
                descricao,
                peso,
                altura,
                comprimento,
                valor
        );
    }
}