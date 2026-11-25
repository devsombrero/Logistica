package br.com.devsombrero.logistica.adapter.in.dto;

import br.com.devsombrero.logistica.domain.model.ItemPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RequisicaoAtualizacaoPedido(
        @Schema(description = "Nova observação", example = "Cliente pediu para ligar antes")
        @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres")
        String observacao,

        @Schema(description = "Nova lista de itens. Se informada, substitui a atual.")
        List<@Valid RequisicaoItemPedido> itens
) {
    public Pedido toDomain() {
        List<ItemPedido> itensDomain = itens == null ? List.of() : itens.stream()
                .map(item -> new ItemPedido(item.produtoId(), null, item.quantidade(), null, null))
                .toList();

        return new Pedido(null, null, null, null, null, observacao, itensDomain, null, null);
    }
}
