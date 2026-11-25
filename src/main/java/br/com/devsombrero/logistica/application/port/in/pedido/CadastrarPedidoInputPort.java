package br.com.devsombrero.logistica.application.port.in.pedido;

import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;

public interface CadastrarPedidoInputPort {
    RetornoPedido cadastrar(Pedido pedido);
}
