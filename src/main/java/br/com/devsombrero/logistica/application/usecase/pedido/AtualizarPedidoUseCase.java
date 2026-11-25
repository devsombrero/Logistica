package br.com.devsombrero.logistica.application.usecase.pedido;

import br.com.devsombrero.logistica.application.port.in.pedido.AtualizarPedidoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.ItemPedido;
import br.com.devsombrero.logistica.domain.model.Pedido;
import br.com.devsombrero.logistica.domain.model.Produto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AtualizarPedidoUseCase implements AtualizarPedidoInputPort {

    private final PedidoPersistenceOutputPort pedidoPersistenceOutputPort;
    private final ProdutoPersistenceOutputPort produtoPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public AtualizarPedidoUseCase(
            final LoggerOutputPort logger,
            final PedidoPersistenceOutputPort pedidoPersistenceOutputPort,
            final ProdutoPersistenceOutputPort produtoPersistenceOutputPort) {
        this.logger = logger;
        this.pedidoPersistenceOutputPort = pedidoPersistenceOutputPort;
        this.produtoPersistenceOutputPort = produtoPersistenceOutputPort;
    }
    @Override
    public RetornoPedido atualizar(String id, Pedido pedido) {
        validarRequisicao(id, pedido);

        logger.info(getClass(), "Iniciando atualização do pedido " + id);

        Pedido pedidoAtual = pedidoPersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Pedido " + id + " não encontrado");
                    return new EntidadeNotFoundException("Pedido " + id + " não encontrado");
                });

        List<ItemPedido> novosItens = possuiItens(pedido)
                ? montarItens(pedido.itens())
                : List.of();

        Pedido pedidoAtualizado = pedidoAtual.atualizar(pedido.observacao(), novosItens);

        Pedido pedidoSalvo = pedidoPersistenceOutputPort.atualizar(id, pedidoAtualizado);

        logger.info(getClass(), "Pedido " + id + " atualizado com valor total "
                + pedidoSalvo.valorTotal());

        return RetornoPedido.toResponse(pedidoSalvo);
    }

    private void validarRequisicao(String id, Pedido pedido) {
        if (id == null || id.isBlank()) {
            throw new RequisicaoInvalidaException("O ID do pedido é obrigatório");
        }
        if (pedido == null) {
            throw new RequisicaoInvalidaException("Pedido não pode ser nulo");
        }
        if (pedido.observacao() == null && !possuiItens(pedido)) {
            throw new RequisicaoInvalidaException("Nenhum dado informado para atualização");
        }
    }

    private boolean possuiItens(Pedido pedido) {
        return !pedido.itens().isEmpty();
    }

    private List<ItemPedido> montarItens(List<ItemPedido> itensSolicitados) {
        return itensSolicitados.stream()
                .map(this::montarItem)
                .toList();
    }

    private ItemPedido montarItem(ItemPedido itemSolicitado) {
        Produto produto = produtoPersistenceOutputPort.buscarPorId(itemSolicitado.produtoId())
                .orElseThrow(() -> new EntidadeNotFoundException(
                        "Produto " + itemSolicitado.produtoId() + " não encontrado"));

        return ItemPedido.criar(produto, itemSolicitado.quantidade());
    }
}
