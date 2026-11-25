package br.com.devsombrero.logistica.support;

import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.ItemPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class PedidoFixture {

    public static final String ID = "885f1c2e9b1e8a3d4c5b6a9f";
    public static final String CLIENTE_ID = ClienteFixture.ID;
    public static final String PRODUTO_ID = ProdutoFixture.ID;
    public static final String OBSERVACAO = "Entregar pela manhã";
    public static final LocalDateTime DATA = LocalDateTime.of(2026, 3, 1, 10, 0);

    private PedidoFixture() {
    }

    public static ItemPedido itemSolicitado() {
        return new ItemPedido(PRODUTO_ID, null, 2, null, null);
    }

    public static ItemPedido itemSalvo() {
        return new ItemPedido(PRODUTO_ID, "Caixa", 2, new BigDecimal("19.90"), new BigDecimal("39.80"));
    }

    public static Pedido pedido(StatusPedido status) {
        return new Pedido(
                ID,
                CLIENTE_ID,
                DATA,
                new BigDecimal("39.80"),
                status,
                OBSERVACAO,
                List.of(itemSalvo()),
                DATA,
                DATA
        );
    }

    public static Pedido pedidoSalvo() {
        return pedido(StatusPedido.PENDENTE);
    }

    public static Pedido requisicaoCadastro() {
        return new Pedido(null, CLIENTE_ID, null, null, null, OBSERVACAO, List.of(itemSolicitado()), null, null);
    }

    public static RetornoPedido retornoPedido() {
        return RetornoPedido.toResponse(pedidoSalvo());
    }
}
