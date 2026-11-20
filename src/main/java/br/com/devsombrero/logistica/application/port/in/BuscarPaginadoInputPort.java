package br.com.devsombrero.logistica.application.port.in;

import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Paginado;

public interface BuscarPaginadoInputPort {
    Paginado<RetornoCliente> buscarPaginado(int tamanhoPagina, int paginaAtual);
}
