package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Produto;

public interface ProdutoService {
    RetornoProduto cadastrar(Produto produto);
}
