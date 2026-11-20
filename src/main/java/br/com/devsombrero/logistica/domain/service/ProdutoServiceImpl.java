package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.produto.CadastrarProdutoInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final CadastrarProdutoInputPort cadastrarProdutoInputPort;

    @Override
    public RetornoProduto cadastrar(Produto produto) {
        return cadastrarProdutoInputPort.cadastrar(produto);
    }
}
