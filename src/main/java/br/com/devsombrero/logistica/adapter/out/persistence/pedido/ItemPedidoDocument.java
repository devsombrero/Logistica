package br.com.devsombrero.logistica.adapter.out.persistence.pedido;

import br.com.devsombrero.logistica.domain.model.ItemPedido;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

public record ItemPedidoDocument(
        String produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal valorUnitario,
        @Field("subtotal")
        BigDecimal subTotal
) {
    public static ItemPedidoDocument fromDomain(ItemPedido item) {
        return new ItemPedidoDocument(
                item.produtoId(),
                item.nomeProduto(),
                item.quantidade(),
                item.valorUnitario(),
                item.subTotal()
        );
    }

    public static ItemPedido toDomain(ItemPedidoDocument document) {
        return new ItemPedido(
                document.produtoId(),
                document.nomeProduto(),
                document.quantidade(),
                document.valorUnitario(),
                document.subTotal()
        );
    }
}