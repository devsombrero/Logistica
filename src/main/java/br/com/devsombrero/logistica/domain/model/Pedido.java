package br.com.devsombrero.logistica.domain.model;

import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Pedido(
        String id,
        String clienteId,
        LocalDateTime dataPedido,
        BigDecimal valorTotal,
        StatusPedido statusPedido,
        String observacao,
        List<ItemPedido> itens,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {

    public BigDecimal calcularValorTotal() {
        return itens.stream()
                .map(ItemPedido::calcularSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean possuiItens() {
        return itens != null && !itens.isEmpty();
    }

    public int quantidadeTotalItens() {
        return itens.stream()
                .mapToInt(ItemPedido::quantidade)
                .sum();
    }

    public boolean estaCancelado() {
        return statusPedido == StatusPedido.CANCELADO;
    }

    public boolean estaConcluido() {
        return statusPedido == StatusPedido.CONCLUIDO;
    }

    public boolean podeSerCancelado() {
        return statusPedido == StatusPedido.PENDENTE
                || statusPedido == StatusPedido.APROVADO;
    }

    public boolean podeSerEnviado() {
        return statusPedido == StatusPedido.EM_SEPARACAO;
    }
}
