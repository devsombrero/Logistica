package br.com.devsombrero.logistica.application.port.in.produto;

import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Produto;

public interface AtualizarProdutoInputPort {
    RetornoProduto atualizar(String id, Produto produto);
}
