package br.com.devsombrero.logistica.adapter.in.dto;

import br.com.devsombrero.logistica.domain.model.ItemPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RequisicaoCadastroPedido(
        @Schema(description = "ID do cliente", example = "665f1b9a8a4b2c1d3e4f5a01")
        @NotBlank(message = "O cliente é obrigatório")
        String clienteId,

        @Schema(description = "Observação do pedido", example = "Entregar no período da manhã")
        @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres")
        String observacao,

        @Schema(description = "Itens do pedido")
        @NotEmpty(message = "O pedido deve possuir pelo menos um produto")
        List<@Valid RequisicaoItemPedido> itens
) {

    public Pedido toDomain() {
        List<ItemPedido> itensDomain = itens.stream()
                .map(item -> new ItemPedido(item.produtoId(), null, item.quantidade(), null, null))
                .toList();

        return new Pedido(null, clienteId, null, null, null, observacao, itensDomain, null, null);
    }
}
