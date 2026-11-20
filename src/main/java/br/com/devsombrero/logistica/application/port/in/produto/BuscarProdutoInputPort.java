package br.com.devsombrero.logistica.application.port.in.produto;

import br.com.devsombrero.logistica.domain.dto.RetornoProduto;

public interface BuscarProdutoInputPort {
    RetornoProduto buscarPorId(String id);
    RetornoProduto buscarPorSku(String sku);
}
