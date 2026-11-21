package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Produto;

public interface ProdutoService {
    Paginado<RetornoProduto> buscarPaginado(int tamanhoPagina, int paginaAtual);
    RetornoProduto cadastrar(Produto produto);
    RetornoProduto buscarPorId(String id);
    RetornoProduto buscarPorSku(String sku);
    RetornoProduto atualizar(String id, Produto produto);
    void apagar(String id);
}
