package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.pedido.*;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final BuscarPedidoInputPort buscarPedidoInputPort;
    private final CadastrarPedidoInputPort cadastrarPedidoInputPort;
    private final AtualizarPedidoInputPort atualizarPedidoInputPort;
    private final CancelarPedidoInputPort cancelarPedidoInputPort;
    private final BuscarPedidoPaginadoInputPort buscarPedidoPaginadoInputPort;

    @Override
    public Paginado<RetornoPedido> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        return this.buscarPedidoPaginadoInputPort.buscarPaginado(tamanhoPagina, paginaAtual);
    }

    @Override
    public Paginado<RetornoPedido> buscarPorCliente(String clienteId, int tamanhoPagina, int paginaAtual) {
        return this.buscarPedidoPaginadoInputPort.buscarPorCliente(clienteId, tamanhoPagina, paginaAtual);
    }

    @Override
    public Paginado<RetornoPedido> buscarPorStatus(StatusPedido statusPedido, int tamanhoPagina, int paginaAtual) {
        return this.buscarPedidoPaginadoInputPort.buscarPorStatus(statusPedido, tamanhoPagina, paginaAtual);
    }

    @Override
    public RetornoPedido cadastrar(Pedido pedido) {
        return this.cadastrarPedidoInputPort.cadastrar(pedido);
    }

    @Override
    public RetornoPedido atualizar(String id, Pedido pedido) {
        return this.atualizarPedidoInputPort.atualizar(id, pedido);
    }

    @Override
    public RetornoPedido cancelar(String id) {
        return this.cancelarPedidoInputPort.cancelar(id);
    }

    @Override
    public RetornoPedido buscarPorId(String id) {
        return this.buscarPedidoInputPort.buscarPorId(id);
    }
}
