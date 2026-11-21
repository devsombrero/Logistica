package br.com.devsombrero.logistica.application.usecase.produto;

import br.com.devsombrero.logistica.application.port.in.produto.ApagarProdutoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ApagarProdutoUseCase implements ApagarProdutoInputPort {

    private final ProdutoPersistenceOutputPort produtoPersistenceOutputPort;

    private final LoggerOutputPort logger;

    public ApagarProdutoUseCase(
            final LoggerOutputPort logger,
            final ProdutoPersistenceOutputPort produtoPersistenceOutputPort
    ) {
        this.logger = logger;
        this.produtoPersistenceOutputPort = produtoPersistenceOutputPort;
    }

    @Override
    public void apagar(String id) {
        logger.info(getClass(), "Iniciando exclusão de produto");

        produtoPersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Produto não encontrado para exclusão");

                    return new EntidadeNotFoundException(
                            "Produto não encontrado com esse ID"
                    );
                });

        produtoPersistenceOutputPort.deletar(id);

        logger.info(getClass(), "Produto excluído com sucesso");
    }
}
