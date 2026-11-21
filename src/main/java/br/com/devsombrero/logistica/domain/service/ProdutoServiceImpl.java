package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.produto.*;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final BuscarProdutoPaginadoInputPort buscarProdutoPaginadoInputPort;
    private final CadastrarProdutoInputPort cadastrarProdutoInputPort;
    private final BuscarProdutoInputPort buscarProdutoInputPort;
    private final AtualizarProdutoInputPort atualizarProdutoInputPort;
    private final ApagarProdutoInputPort apagarProdutoInputPort;

    @Override
    public Paginado<RetornoProduto> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        return buscarProdutoPaginadoInputPort.buscarPaginado(tamanhoPagina, paginaAtual);
    }

    @Override
    public RetornoProduto cadastrar(Produto produto) {
        return cadastrarProdutoInputPort.cadastrar(produto);
    }

    @Override
    public RetornoProduto buscarPorId(String id) {
        return buscarProdutoInputPort.buscarPorId(id);
    }

    @Override
    public RetornoProduto buscarPorSku(String sku) {
        return buscarProdutoInputPort.buscarPorSku(sku);
    }

    @Override
    public RetornoProduto atualizar(String id, Produto produto) {
        return atualizarProdutoInputPort.atualizar(id, produto);
    }

    @Override
    public void apagar(String id) {
        apagarProdutoInputPort.apagar(id);
    }
}
