package br.com.devsombrero.logistica.adapter.out.persistence.pedido;

import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.devsombrero.logistica.utils.PersistenceUtils.valorOuAtual;

@Document("pedidos")
public record PedidoDocument(
        @Id
        String id,
        @Indexed
        String clienteId,
        LocalDateTime dataPedido,
        BigDecimal valorTotal,
        @Indexed
        @Field("status")
        StatusPedido statusPedido,
        String observacao,
        List<ItemPedidoDocument> itens,
        @CreatedDate
        LocalDateTime dataCriacao,
        @LastModifiedDate
        LocalDateTime dataAtualizacao
) {

    public PedidoDocument {
        itens = itens == null ? List.of() : List.copyOf(itens);
    }

    public static PedidoDocument fromDomain(Pedido pedido) {
        return new PedidoDocument(
                pedido.id(),
                pedido.clienteId(),
                pedido.dataPedido(),
                pedido.valorTotal(),
                pedido.statusPedido(),
                pedido.observacao(),
                pedido.itens().stream()
                        .map(ItemPedidoDocument::fromDomain)
                        .toList(),
                pedido.dataCriacao(),
                pedido.dataAtualizacao()
        );
    }

    public static Pedido toDomain(PedidoDocument pedidoDocument) {
        return new Pedido(
                pedidoDocument.id(),
                pedidoDocument.clienteId(),
                pedidoDocument.dataPedido(),
                pedidoDocument.valorTotal(),
                pedidoDocument.statusPedido(),
                pedidoDocument.observacao(),
                pedidoDocument.itens().stream()
                        .map(ItemPedidoDocument::toDomain)
                        .toList(),
                pedidoDocument.dataCriacao(),
                pedidoDocument.dataAtualizacao()
        );
    }

    public PedidoDocument atualizar(Pedido pedido) {
        return new PedidoDocument(
                this.id,
                this.clienteId,
                this.dataPedido,
                valorOuAtual(this.valorTotal, pedido.valorTotal()),
                valorOuAtual(this.statusPedido, pedido.statusPedido()),
                valorOuAtual(this.observacao, pedido.observacao()),
                atualizarItens(pedido),
                this.dataCriacao,
                this.dataAtualizacao
        );
    }

    private List<ItemPedidoDocument> atualizarItens(Pedido pedido) {
        if (pedido.itens() == null || pedido.itens().isEmpty()) {
            return this.itens;
        }
        return pedido.itens().stream()
                .map(ItemPedidoDocument::fromDomain)
                .toList();
    }
}