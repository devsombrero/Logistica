package br.com.devsombrero.logistica.application.usecase.pedido;

import br.com.devsombrero.logistica.application.port.in.pedido.BuscarPedidoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import org.springframework.stereotype.Component;

@Component
public class BuscarPedidoUseCase implements BuscarPedidoInputPort {

    private final PedidoPersistenceOutputPort pedidoPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public BuscarPedidoUseCase(
            final LoggerOutputPort logger,
            final PedidoPersistenceOutputPort pedidoPersistenceOutputPort) {
        this.logger = logger;
        this.pedidoPersistenceOutputPort = pedidoPersistenceOutputPort;
    }

    @Override
    public RetornoPedido buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new RequisicaoInvalidaException("O ID do pedido é obrigatório");
        }

        logger.info(getClass(), "Buscando pedido " + id);

        return pedidoPersistenceOutputPort.buscarPorId(id)
                .map(RetornoPedido::toResponse)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Pedido " + id + " não encontrado");
                    return new EntidadeNotFoundException("Pedido " + id + " não encontrado");
                });
    }
}