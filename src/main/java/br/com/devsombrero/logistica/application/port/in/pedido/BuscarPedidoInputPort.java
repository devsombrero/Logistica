package br.com.devsombrero.logistica.application.port.in.pedido;

import br.com.devsombrero.logistica.domain.dto.RetornoPedido;

public interface BuscarPedidoInputPort {
    RetornoPedido buscarPorId(String id);
}
