package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.pedido.ItemPedidoDocument;
import br.com.devsombrero.logistica.adapter.out.persistence.pedido.PedidoDocument;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.ItemPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;
import br.com.devsombrero.logistica.support.PedidoFixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PedidoDocumentTest {

    private final Pedido pedido = PedidoFixture.pedidoSalvo();
    private final ItemPedido item = PedidoFixture.itemSalvo();
    private final ItemPedido outroItem =
            new ItemPedido("outro", "Fita", 3, new BigDecimal("5.00"), new BigDecimal("15.00"));

    @Test
    void construtorDeveTrocarItensNulosPorListaVazia() {
        PedidoDocument document = new PedidoDocument(PedidoFixture.ID, null, null, null, null, null, null, null, null);

        assertThat(document.itens()).isEmpty();
    }

    @Test
    void fromDomainEToDomainDevemPreservarTodosOsCampos() {
        PedidoDocument document = PedidoDocument.fromDomain(pedido);

        assertThat(document.id()).isEqualTo(PedidoFixture.ID);
        assertThat(document.clienteId()).isEqualTo(PedidoFixture.CLIENTE_ID);
        assertThat(document.dataPedido()).isEqualTo(PedidoFixture.DATA);
        assertThat(document.valorTotal()).isEqualTo(pedido.valorTotal());
        assertThat(document.statusPedido()).isEqualTo(StatusPedido.PENDENTE);
        assertThat(document.observacao()).isEqualTo(PedidoFixture.OBSERVACAO);
        assertThat(document.itens()).containsExactly(ItemPedidoDocument.fromDomain(item));
        assertThat(document.dataCriacao()).isEqualTo(PedidoFixture.DATA);
        assertThat(document.dataAtualizacao()).isEqualTo(PedidoFixture.DATA);
        assertThat(PedidoDocument.toDomain(document)).isEqualTo(pedido);
    }

    @Test
    void itemPedidoDocumentDeveConverterIdaEVolta() {
        ItemPedidoDocument document = ItemPedidoDocument.fromDomain(item);

        assertThat(document).isEqualTo(new ItemPedidoDocument(
                item.produtoId(), item.nomeProduto(), item.quantidade(), item.valorUnitario(), item.subTotal()));
        assertThat(ItemPedidoDocument.toDomain(document)).isEqualTo(item);
    }

    @Test
    void atualizarDeveSobrescreverCamposInformadosEPreservarImutaveis() {
        PedidoDocument atual = PedidoDocument.fromDomain(pedido);
        Pedido alteracao = new Pedido("ignorado", "outro-cliente", null, new BigDecimal("15.00"),
                StatusPedido.CANCELADO, "nova", List.of(outroItem), null, null);

        PedidoDocument atualizado = atual.atualizar(alteracao);

        assertThat(atualizado.id()).isEqualTo(PedidoFixture.ID);
        assertThat(atualizado.clienteId()).isEqualTo(PedidoFixture.CLIENTE_ID);
        assertThat(atualizado.dataPedido()).isEqualTo(PedidoFixture.DATA);
        assertThat(atualizado.valorTotal()).isEqualByComparingTo("15.00");
        assertThat(atualizado.statusPedido()).isEqualTo(StatusPedido.CANCELADO);
        assertThat(atualizado.observacao()).isEqualTo("nova");
        assertThat(atualizado.itens()).containsExactly(ItemPedidoDocument.fromDomain(outroItem));
        assertThat(atualizado.dataCriacao()).isEqualTo(PedidoFixture.DATA);
        assertThat(atualizado.dataAtualizacao()).isEqualTo(PedidoFixture.DATA);
    }

    @Test
    void atualizarDeveManterValoresAtuaisQuandoAlteracaoVazia() {
        PedidoDocument atual = PedidoDocument.fromDomain(pedido);
        Pedido alteracao = new Pedido(null, null, null, null, null, null, List.of(), null, null);

        assertThat(atual.atualizar(alteracao)).isEqualTo(atual);
    }
}
