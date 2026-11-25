package br.com.devsombrero.logistica.domain.dto;

import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.ItemPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RetornoPedido(
        String id,
        String clienteId,
        LocalDateTime dataPedido,
        BigDecimal valorTotal,
        StatusPedido statusPedido,
        String observacao,
        List<ItemPedido> itens
) {
    public static RetornoPedido toResponse(Pedido pedido) {
        return new RetornoPedido(
                pedido.id(),
                pedido.clienteId(),
                pedido.dataPedido(),
                pedido.valorTotal(),
                pedido.statusPedido(),
                pedido.observacao(),
                pedido.itens()
        );
    }
}