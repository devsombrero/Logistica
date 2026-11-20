package br.com.devsombrero.logistica.domain.mapper;

import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Produto;

public final class ProdutoMapper {
    public static RetornoProduto toResponse(Produto produto) {
        return new RetornoProduto(
                produto.id(),
                produto.sku(),
                produto.nome(),
                produto.descricao(),
                produto.peso(),
                produto.altura(),
                produto.comprimento(),
                produto.valor()
        );
    }
}
