package br.com.devsombrero.logistica.application.usecase.produto;

import br.com.devsombrero.logistica.application.port.in.produto.CadastrarProdutoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.mapper.ProdutoMapper;
import br.com.devsombrero.logistica.domain.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class CadastrarProdutoUseCase implements CadastrarProdutoInputPort {

    private final ProdutoPersistenceOutputPort produtoPersistenceOutputPort;

    private final LoggerOutputPort logger;

    public CadastrarProdutoUseCase(
            final ProdutoPersistenceOutputPort produtoPersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.produtoPersistenceOutputPort = produtoPersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public RetornoProduto cadastrar(Produto produto) {

        logger.info(getClass(), "Iniciando cadastro de produto");

        if (produto == null) {
            logger.warn(getClass(), "Cadastro de produto recusado: Produto não pode ser null!");
            throw new RequisicaoInvalidaException("O produto não pode ser nulo!");
        }

        if (produtoPersistenceOutputPort.existePorSku(produto.sku())) {
            logger.warn(getClass(), "Cadastro de produto recusado: Produto com sku já existe!");
            throw new EntidadeUnprocessableException("O produto com esse sku já existe na base de dados!");
        }

        Produto produtoSalvo = this.produtoPersistenceOutputPort.salvar(produto);

        logger.info(getClass(), "Produto cadastrado com sucesso");
        return ProdutoMapper.toResponse(produtoSalvo);
    }
}
