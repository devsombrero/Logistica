package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Pedido;

public interface PedidoService {
    Paginado<RetornoPedido> buscarPaginado(int tamanhoPagina, int paginaAtual);
    Paginado<RetornoPedido> buscarPorCliente(String clienteId, int tamanhoPagina, int paginaAtual);
    Paginado<RetornoPedido> buscarPorStatus(StatusPedido statusPedido, int tamanhoPagina, int paginaAtual);
    RetornoPedido cadastrar(Pedido pedido);
    RetornoPedido atualizar(String id, Pedido pedido);
    RetornoPedido cancelar(String id);
    RetornoPedido buscarPorId(String id);
}
