package br.com.devsombrero.logistica.application.port.out.pedido;

import br.com.devsombrero.logistica.application.port.out.crud.CrudPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Pedido;

public interface PedidoPersistenceOutputPort
    extends CrudPersistenceOutputPort<Pedido, String> {
    Paginado<Pedido> buscarPedidosPorCliente(String clienteId);
    Paginado<Pedido> buscarPedidoPorStatus(StatusPedido statusPedido);
}
