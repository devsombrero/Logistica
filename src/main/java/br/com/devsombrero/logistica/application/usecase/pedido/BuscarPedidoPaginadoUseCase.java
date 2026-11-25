package br.com.devsombrero.logistica.application.usecase.pedido;

import br.com.devsombrero.logistica.application.port.in.pedido.BuscarPedidoPaginadoInputPort;
import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import org.springframework.stereotype.Component;

@Component
public class BuscarPedidoPaginadoUseCase implements BuscarPedidoPaginadoInputPort {

    private static final int TAMANHO_MAXIMO_PAGINA = 50;

    private final PedidoPersistenceOutputPort pedidoPersistenceOutputPort;
    private final ClientePersistenceOutputPort clientePersistenceOutputPort;
    private final LoggerOutputPort logger;

    public BuscarPedidoPaginadoUseCase(
            final LoggerOutputPort logger,
            final PedidoPersistenceOutputPort pedidoPersistenceOutputPort,
            final ClientePersistenceOutputPort clientePersistenceOutputPort) {
        this.logger = logger;
        this.pedidoPersistenceOutputPort = pedidoPersistenceOutputPort;
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
    }

    @Override
    public Paginado<RetornoPedido> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        validarPaginacao(tamanhoPagina, paginaAtual);

        logger.info(getClass(), "Buscando pedidos - página " + paginaAtual + ", tamanho " + tamanhoPagina);

        return pedidoPersistenceOutputPort
                .buscarPaginado(tamanhoPagina, paginaAtual)
                .map(RetornoPedido::toResponse);
    }

    @Override
    public Paginado<RetornoPedido> buscarPorCliente(String clienteId, int tamanhoPagina, int paginaAtual) {
        if (clienteId == null || clienteId.isBlank()) {
            throw new RequisicaoInvalidaException("O ID do cliente é obrigatório");
        }
        validarPaginacao(tamanhoPagina, paginaAtual);

        if (clientePersistenceOutputPort.buscarPorId(clienteId).isEmpty()) {
            logger.warn(getClass(), "Cliente " + clienteId + " não encontrado");
            throw new EntidadeNotFoundException("Cliente " + clienteId + " não encontrado");
        }

        logger.info(getClass(), "Buscando pedidos do cliente " + clienteId);

        return pedidoPersistenceOutputPort
                .buscarPedidosPorCliente(clienteId, tamanhoPagina, paginaAtual)
                .map(RetornoPedido::toResponse);
    }

    @Override
    public Paginado<RetornoPedido> buscarPorStatus(StatusPedido statusPedido, int tamanhoPagina, int paginaAtual) {
        if (statusPedido == null) {
            throw new RequisicaoInvalidaException("O status do pedido é obrigatório");
        }
        validarPaginacao(tamanhoPagina, paginaAtual);

        logger.info(getClass(), "Buscando pedidos com status " + statusPedido);

        return pedidoPersistenceOutputPort
                .buscarPedidoPorStatus(statusPedido, tamanhoPagina, paginaAtual)
                .map(RetornoPedido::toResponse);
    }

    private void validarPaginacao(int tamanhoPagina, int paginaAtual) {
        if (paginaAtual < 0) {
            throw new RequisicaoInvalidaException("A página atual não pode ser negativa");
        }
        if (tamanhoPagina < 1 || tamanhoPagina > TAMANHO_MAXIMO_PAGINA) {
            throw new RequisicaoInvalidaException(
                    "O tamanho da página deve estar entre 1 e " + TAMANHO_MAXIMO_PAGINA);
        }
    }
}