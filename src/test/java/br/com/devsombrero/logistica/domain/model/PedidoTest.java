package br.com.devsombrero.logistica.domain.model;

import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.support.PedidoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PedidoTest {

    private static final String ID = PedidoFixture.ID;
    private static final String CLIENTE_ID = PedidoFixture.CLIENTE_ID;
    private static final String PRODUTO_ID = PedidoFixture.PRODUTO_ID;

    private final ItemPedido item = PedidoFixture.itemSalvo();
    private final ItemPedido outroItem =
            new ItemPedido("outro", "Fita", 3, new BigDecimal("5.00"), new BigDecimal("15.00"));

    @Test
    void construtorDeveTrocarItensNulosPorListaVazia() {
        Pedido pedido = new Pedido(ID, CLIENTE_ID, null, null, null, null, null, null, null);

        assertThat(pedido.itens()).isEmpty();
        assertThat(pedido.possuiItens()).isFalse();
    }

    @Test
    void construtorDeveCopiarItensDefensivamente() {
        List<ItemPedido> itens = new ArrayList<>(List.of(item));
        Pedido pedido = new Pedido(ID, CLIENTE_ID, null, null, null, null, itens, null, null);

        itens.add(outroItem);

        assertThat(pedido.itens()).containsExactly(item);
        assertThat(pedido.possuiItens()).isTrue();
    }

    @Test
    void criarNovoDeveMontarPedidoPendenteComTotal() {
        Pedido pedido = Pedido.criarNovo(CLIENTE_ID, "obs", List.of(item, outroItem));

        assertThat(pedido.id()).isNull();
        assertThat(pedido.clienteId()).isEqualTo(CLIENTE_ID);
        assertThat(pedido.dataPedido()).isNotNull();
        assertThat(pedido.valorTotal()).isEqualByComparingTo("54.80");
        assertThat(pedido.statusPedido()).isEqualTo(StatusPedido.PENDENTE);
        assertThat(pedido.observacao()).isEqualTo("obs");
        assertThat(pedido.itens()).containsExactly(item, outroItem);
        assertThat(pedido.dataCriacao()).isNull();
        assertThat(pedido.dataAtualizacao()).isNull();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void criarNovoDeveRecusarClienteInvalido(String clienteId) {
        List<ItemPedido> itens = List.of(item);

        assertThatThrownBy(() -> Pedido.criarNovo(clienteId, null, itens))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O pedido deve possuir um cliente");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void criarNovoDeveRecusarPedidoSemItens(List<ItemPedido> itens) {
        assertThatThrownBy(() -> Pedido.criarNovo(CLIENTE_ID, null, itens))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O pedido deve possuir pelo menos um produto");
    }

    @Test
    void calcularValorTotalDeveUsarValorUnitarioEQuantidade() {
        ItemPedido semSubTotal = new ItemPedido(PRODUTO_ID, "Caixa", 4, new BigDecimal("2.50"), null);
        Pedido pedido = pedidoCom(StatusPedido.PENDENTE, List.of(semSubTotal, outroItem));

        assertThat(pedido.calcularValorTotal()).isEqualByComparingTo("25.00");
    }

    @Test
    void quantidadeTotalItensDeveSomarQuantidades() {
        Pedido pedido = pedidoCom(StatusPedido.PENDENTE, List.of(item, outroItem));

        assertThat(pedido.quantidadeTotalItens()).isEqualTo(5);
    }

    @ParameterizedTest
    @EnumSource(StatusPedido.class)
    void regrasDeStatusDevemSeguirOStatusAtual(StatusPedido status) {
        Pedido pedido = PedidoFixture.pedido(status);

        assertThat(pedido.estaCancelado()).isEqualTo(status == StatusPedido.CANCELADO);
        assertThat(pedido.estaConcluido()).isEqualTo(status == StatusPedido.CONCLUIDO);
        assertThat(pedido.podeSerCancelado())
                .isEqualTo(status == StatusPedido.PENDENTE || status == StatusPedido.APROVADO);
        assertThat(pedido.podeSerEnviado()).isEqualTo(status == StatusPedido.EM_SEPARACAO);
        assertThat(pedido.podeSerEditado()).isEqualTo(status == StatusPedido.PENDENTE);
    }

    @Test
    void atualizarDeveSubstituirObservacaoEItensERecalcularTotal() {
        Pedido pedido = PedidoFixture.pedidoSalvo();

        Pedido atualizado = pedido.atualizar("nova", List.of(outroItem));

        assertThat(atualizado.observacao()).isEqualTo("nova");
        assertThat(atualizado.itens()).containsExactly(outroItem);
        assertThat(atualizado.valorTotal()).isEqualByComparingTo("15.00");
        assertThat(atualizado.id()).isEqualTo(ID);
        assertThat(atualizado.clienteId()).isEqualTo(CLIENTE_ID);
        assertThat(atualizado.dataPedido()).isEqualTo(PedidoFixture.DATA);
        assertThat(atualizado.statusPedido()).isEqualTo(StatusPedido.PENDENTE);
        assertThat(atualizado.dataCriacao()).isEqualTo(PedidoFixture.DATA);
        assertThat(atualizado.dataAtualizacao()).isEqualTo(PedidoFixture.DATA);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void atualizarDeveManterItensEObservacaoQuandoNaoInformados(List<ItemPedido> novosItens) {
        Pedido pedido = PedidoFixture.pedidoSalvo();

        Pedido atualizado = pedido.atualizar(null, novosItens);

        assertThat(atualizado.observacao()).isEqualTo(PedidoFixture.OBSERVACAO);
        assertThat(atualizado.itens()).containsExactly(item);
        assertThat(atualizado.valorTotal()).isEqualByComparingTo("39.80");
    }

    @Test
    void atualizarDeveRecusarPedidoForaDePendente() {
        Pedido pedido = PedidoFixture.pedido(StatusPedido.APROVADO);
        List<ItemPedido> itens = List.of(outroItem);

        assertThatThrownBy(() -> pedido.atualizar("x", itens))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("Pedido " + ID + " não pode ser editado no status APROVADO");
    }

    @ParameterizedTest
    @EnumSource(value = StatusPedido.class, names = {"PENDENTE", "APROVADO"})
    void cancelarDeveMudarStatusParaCancelado(StatusPedido status) {
        Pedido pedido = PedidoFixture.pedido(status);

        Pedido cancelado = pedido.cancelar();

        assertThat(cancelado).isEqualTo(PedidoFixture.pedido(StatusPedido.CANCELADO));
    }

    @Test
    void cancelarDeveRecusarPedidoJaCancelado() {
        Pedido pedido = PedidoFixture.pedido(StatusPedido.CANCELADO);

        assertThatThrownBy(pedido::cancelar)
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("Pedido " + ID + " já está cancelado");
    }

    @ParameterizedTest
    @EnumSource(value = StatusPedido.class, names = {"EM_SEPARACAO", "ENVIADO", "CONCLUIDO"})
    void cancelarDeveRecusarStatusNaoCancelavel(StatusPedido status) {
        Pedido pedido = PedidoFixture.pedido(status);

        assertThatThrownBy(pedido::cancelar)
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("Pedido " + ID + " não pode ser cancelado no status " + status);
    }

    @Test
    void retornoPedidoDeveCopiarCamposDoDominio() {
        Pedido pedido = PedidoFixture.pedidoSalvo();

        RetornoPedido retorno = RetornoPedido.toResponse(pedido);

        assertThat(retorno).isEqualTo(new RetornoPedido(
                ID,
                CLIENTE_ID,
                PedidoFixture.DATA,
                pedido.valorTotal(),
                StatusPedido.PENDENTE,
                PedidoFixture.OBSERVACAO,
                List.of(item)
        ));
    }

    private static Pedido pedidoCom(StatusPedido status, List<ItemPedido> itens) {
        return new Pedido(ID, CLIENTE_ID, null, null, status, null, itens, null, null);
    }
}
