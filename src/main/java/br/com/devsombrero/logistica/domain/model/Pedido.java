package br.com.devsombrero.logistica.domain.model;

import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;

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

    public Pedido {
        itens = itens == null ? List.of() : List.copyOf(itens);
    }

    public static Pedido criarNovo(String clienteId, String observacao, List<ItemPedido> itens) {
        if (clienteId == null || clienteId.isBlank()) {
            throw new RequisicaoInvalidaException("O pedido deve possuir um cliente");
        }
        if (itens == null || itens.isEmpty()) {
            throw new RequisicaoInvalidaException("O pedido deve possuir pelo menos um produto");
        }

        BigDecimal total = itens.stream()
                .map(ItemPedido::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Pedido(
                null,
                clienteId,
                LocalDateTime.now(),
                total,
                StatusPedido.PENDENTE,
                observacao,
                itens,
                null,
                null
        );
    }

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

    public boolean podeSerEditado() {
        return statusPedido == StatusPedido.PENDENTE;
    }

    public Pedido atualizar(String novaObservacao, List<ItemPedido> novosItens) {
        if (!podeSerEditado()) {
            throw new RequisicaoInvalidaException(
                    "Pedido " + id + " não pode ser editado no status " + statusPedido);
        }

        List<ItemPedido> itensFinais = (novosItens == null || novosItens.isEmpty())
                ? this.itens
                : novosItens;

        String observacaoFinal = novaObservacao != null ? novaObservacao : this.observacao;

        return new Pedido(
                this.id,
                this.clienteId,
                this.dataPedido,
                somarSubTotais(itensFinais),
                this.statusPedido,
                observacaoFinal,
                itensFinais,
                this.dataCriacao,
                this.dataAtualizacao
        );
    }

    private static BigDecimal somarSubTotais(List<ItemPedido> itens) {
        return itens.stream()
                .map(ItemPedido::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
