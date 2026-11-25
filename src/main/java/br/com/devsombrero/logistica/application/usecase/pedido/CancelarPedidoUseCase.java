package br.com.devsombrero.logistica.application.usecase.pedido;

import br.com.devsombrero.logistica.application.port.in.pedido.CancelarPedidoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class CancelarPedidoUseCase implements CancelarPedidoInputPort {

    private final PedidoPersistenceOutputPort pedidoPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public CancelarPedidoUseCase(
            final LoggerOutputPort logger,
            final PedidoPersistenceOutputPort pedidoPersistenceOutputPort) {
        this.logger = logger;
        this.pedidoPersistenceOutputPort = pedidoPersistenceOutputPort;
    }

    @Override
    public RetornoPedido cancelar(String id) {
        if (id == null || id.isBlank()) {
            throw new RequisicaoInvalidaException("O ID do pedido é obrigatório");
        }

        logger.info(getClass(), "Iniciando cancelamento do pedido " + id);

        Pedido pedidoAtual = pedidoPersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Pedido " + id + " não encontrado");
                    return new EntidadeNotFoundException("Pedido " + id + " não encontrado");
                });

        Pedido pedidoCancelado = pedidoAtual.cancelar();

        Pedido pedidoSalvo = pedidoPersistenceOutputPort.atualizar(id, pedidoCancelado);

        logger.info(getClass(), "Pedido " + id + " cancelado com sucesso");

        return RetornoPedido.toResponse(pedidoSalvo);
    }
}