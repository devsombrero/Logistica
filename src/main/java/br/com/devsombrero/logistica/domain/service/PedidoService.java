package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;

public interface PedidoService {
    RetornoPedido cadastrar(Pedido pedido);
}
