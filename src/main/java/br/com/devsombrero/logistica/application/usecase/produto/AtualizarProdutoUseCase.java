package br.com.devsombrero.logistica.application.usecase.produto;

import br.com.devsombrero.logistica.application.port.in.produto.AtualizarProdutoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.mapper.ProdutoMapper;
import br.com.devsombrero.logistica.domain.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class AtualizarProdutoUseCase implements AtualizarProdutoInputPort {

    private final ProdutoPersistenceOutputPort produtoPersistenceOutputPort;

    private final LoggerOutputPort logger;

    public AtualizarProdutoUseCase(
            final LoggerOutputPort logger,
            final ProdutoPersistenceOutputPort produtoPersistenceOutputPort
    ) {
        this.logger = logger;
        this.produtoPersistenceOutputPort = produtoPersistenceOutputPort;
    }

    @Override
    public RetornoProduto atualizar(String id, Produto produto) {
        logger.info(getClass(), "Iniciando atualização de produto");

        produtoPersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Produto não encontrado para atualização");

                    return new EntidadeNotFoundException(
                            "Produto não encontrado: " + id
                    );
                });

        if (produtoPersistenceOutputPort.existePorSkuEIdDiferente(
                produto.sku(), id
        )) {
            logger.warn(getClass(), "Já existe outro produto cadastrado com este SKU");

            throw new EntidadeUnprocessableException(
                    "Já existe outro produto cadastrado com o SKU: " + produto.sku()
            );
        }

        Produto produtoAtualizado = produtoPersistenceOutputPort.atualizar(
                id, produto
        );

        logger.info(getClass(), "Produto atualizado com sucesso");

        return ProdutoMapper.toResponse(produtoAtualizado);
    }
}
