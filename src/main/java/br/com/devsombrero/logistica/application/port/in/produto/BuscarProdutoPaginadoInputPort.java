package br.com.devsombrero.logistica.application.port.in.produto;

import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Paginado;

public interface BuscarProdutoPaginadoInputPort {
    Paginado<RetornoProduto> buscarPaginado(int tamanhoPagina, int paginaAtual);
}
