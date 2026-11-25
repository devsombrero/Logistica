package br.com.devsombrero.logistica.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RequisicaoItemPedido(
        @Schema(description = "ID do produto", example = "665f1c2e8a4b2c1d3e4f5a6b")
        @NotBlank(message = "O produto é obrigatório")
        String produtoId,

        @Schema(description = "Quantidade solicitada", example = "3")
        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {
}
