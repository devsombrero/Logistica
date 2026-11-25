package br.com.devsombrero.logistica.application.port.in.pedido;

import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.Paginado;

public interface BuscarPedidoPaginadoInputPort {
    Paginado<RetornoPedido> buscarPaginado(int tamanhoPagina, int paginaAtual);
    Paginado<RetornoPedido> buscarPorCliente(String clienteId, int tamanhoPagina, int paginaAtual);
    Paginado<RetornoPedido> buscarPorStatus(StatusPedido statusPedido, int tamanhoPagina, int paginaAtual);
}