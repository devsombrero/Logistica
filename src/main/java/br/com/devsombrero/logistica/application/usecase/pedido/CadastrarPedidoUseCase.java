package br.com.devsombrero.logistica.application.usecase.pedido;

import br.com.devsombrero.logistica.application.port.in.pedido.CadastrarPedidoInputPort;
import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
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
public class CadastrarPedidoUseCase implements CadastrarPedidoInputPort {

    private final PedidoPersistenceOutputPort pedidoPersistenceOutputPort;
    private final ClientePersistenceOutputPort clientePersistenceOutputPort;
    private final ProdutoPersistenceOutputPort produtoPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public CadastrarPedidoUseCase(
            final LoggerOutputPort logger,
            final PedidoPersistenceOutputPort pedidoPersistenceOutputPort,
            final ClientePersistenceOutputPort clientePersistenceOutputPort,
            final ProdutoPersistenceOutputPort produtoPersistenceOutputPort) {
        this.logger = logger;
        this.pedidoPersistenceOutputPort = pedidoPersistenceOutputPort;
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
        this.produtoPersistenceOutputPort = produtoPersistenceOutputPort;
    }

    @Override
    public RetornoPedido cadastrar(Pedido pedido) {
        if (pedido == null) {
            throw new RequisicaoInvalidaException("Pedido não pode ser nulo");
        }

        logger.info(getClass(), "Iniciando cadastro de pedido para o cliente " + pedido.clienteId());

        validarCliente(pedido.clienteId());

        List<ItemPedido> itens = montarItens(pedido.itens());

        Pedido novoPedido = Pedido.criarNovo(pedido.clienteId(), pedido.observacao(), itens);

        Pedido pedidoSalvo = pedidoPersistenceOutputPort.salvar(novoPedido);

        logger.info(getClass(), "Pedido " + pedidoSalvo.id() + " cadastrado com valor total "
                + pedidoSalvo.valorTotal());

        return RetornoPedido.toResponse(pedidoSalvo);
    }

    private void validarCliente(String clienteId) {
        if (clienteId == null || clienteId.isBlank()) {
            throw new RequisicaoInvalidaException("O pedido deve possuir um cliente");
        }

        clientePersistenceOutputPort.buscarPorId(clienteId)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Cliente " + clienteId + " não encontrado");
                    return new EntidadeNotFoundException(
                            "Cliente " + clienteId + " não encontrado");
                });
    }

    private List<ItemPedido> montarItens(List<ItemPedido> itensSolicitados) {
        if (itensSolicitados == null || itensSolicitados.isEmpty()) {
            throw new RequisicaoInvalidaException("O pedido deve possuir pelo menos um produto");
        }

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