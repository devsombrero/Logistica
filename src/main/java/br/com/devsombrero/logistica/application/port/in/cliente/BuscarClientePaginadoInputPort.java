package br.com.devsombrero.logistica.application.port.in.cliente;

import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Paginado;

public interface BuscarClientePaginadoInputPort {
    Paginado<RetornoCliente> buscarPaginado(int tamanhoPagina, int paginaAtual);
}
