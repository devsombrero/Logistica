package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.pedido.CadastrarPedidoInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final CadastrarPedidoInputPort cadastrarPedidoInputPort;

    @Override
    public RetornoPedido cadastrar(Pedido pedido) {
        return this.cadastrarPedidoInputPort.cadastrar(pedido);
    }
}
