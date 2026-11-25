package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.pedido.AtualizarPedidoInputPort;
import br.com.devsombrero.logistica.application.port.in.pedido.CadastrarPedidoInputPort;
import br.com.devsombrero.logistica.application.port.in.pedido.CancelarPedidoInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final CadastrarPedidoInputPort cadastrarPedidoInputPort;
    private final AtualizarPedidoInputPort atualizarPedidoInputPort;
    private final CancelarPedidoInputPort cancelarPedidoInputPort;

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
}
