package br.com.devsombrero.logistica.application.port.in.pedido;

import br.com.devsombrero.logistica.domain.dto.RetornoPedido;

public interface CancelarPedidoInputPort {
    RetornoPedido cancelar(String id);
}
