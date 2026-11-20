package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;

public interface ClienteService {
    Paginado<RetornoCliente> buscarPaginado(int tamanhoPagina, int paginaAtual);
    RetornoCliente cadastrar(Cliente cliente);
    RetornoCliente buscarPorId(String id);
    RetornoCliente buscarPorEmail(String email);
    RetornoCliente buscarPorDocumento(String documento);
    RetornoCliente atualizar(String id, Cliente cliente);
    void apagar(String id);
}
