package br.com.devsombrero.logistica.application.usecase.produto;

import br.com.devsombrero.logistica.application.port.in.produto.BuscarProdutoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.mapper.ProdutoMapper;
import org.springframework.stereotype.Component;

@Component
public class BuscarProdutoUseCase implements BuscarProdutoInputPort {

    private final ProdutoPersistenceOutputPort produtoPersistenceOutputPort;

    private final LoggerOutputPort logger;

    public BuscarProdutoUseCase(
            final ProdutoPersistenceOutputPort produtoPersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.produtoPersistenceOutputPort = produtoPersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public RetornoProduto buscarPorId(String id) {
        logger.info(getClass(), "Iniciando busca de um produto por ID");
        var produto = this.produtoPersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Produto não encontrado com esse ID");
                    return new EntidadeNotFoundException("Produto não encontrado com esse ID");
                });
        logger.info(getClass(), "Produto encontrado com esse ID com sucesso");
        return ProdutoMapper.toResponse(produto);
    }

    @Override
    public RetornoProduto buscarPorSku(String sku) {
        logger.info(getClass(), "Iniciando busca de um produto por SKU");
        var produto = this.produtoPersistenceOutputPort.buscarPorSku(sku)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Produto não encontrado com esse SKU");
                    return new EntidadeNotFoundException("Produto não encontrado com esse SKU");
                });
        logger.info(getClass(), "Produto encontrado com esse SKU com sucesso");
        return ProdutoMapper.toResponse(produto);
    }
}
