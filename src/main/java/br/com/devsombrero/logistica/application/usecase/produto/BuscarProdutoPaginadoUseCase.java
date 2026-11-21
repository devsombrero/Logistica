package br.com.devsombrero.logistica.application.usecase.produto;

import br.com.devsombrero.logistica.application.port.in.produto.BuscarProdutoPaginadoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.mapper.ProdutoMapper;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Produto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuscarProdutoPaginadoUseCase implements BuscarProdutoPaginadoInputPort {

    private final ProdutoPersistenceOutputPort produtoPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public BuscarProdutoPaginadoUseCase(
            final ProdutoPersistenceOutputPort produtoPersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.produtoPersistenceOutputPort = produtoPersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public Paginado<RetornoProduto> buscarPaginado(
            int tamanhoPagina,
            int paginaAtual
    ) {

        logger.info(getClass(), "Iniciando busca paginada de produtos");

        if (tamanhoPagina <= 0 || paginaAtual < 0) {
            logger.warn(getClass(), "Parâmetros inválidos para busca paginada");

            throw new RequisicaoInvalidaException(
                    "O tamanho da página deve ser maior que zero e a página não pode ser negativa."
            );
        }

        Paginado<Produto> resultado =
                produtoPersistenceOutputPort.buscarPaginado(
                        tamanhoPagina,
                        paginaAtual
                );

        List<RetornoProduto> produtos = resultado.conteudo()
                .stream()
                .map(ProdutoMapper::toResponse)
                .toList();

        logger.info(getClass(), "Busca paginada de produtos realizada com sucesso");

        return new Paginado<>(
                produtos,
                resultado.paginaAtual(),
                resultado.tamanhoPagina(),
                resultado.totalPaginas(),
                resultado.totalElementos()
        );
    }
}
