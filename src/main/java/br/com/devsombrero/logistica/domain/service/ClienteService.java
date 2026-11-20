package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Cliente;

public interface ClienteService {
    RetornoCliente cadastrar(Cliente cliente);
    RetornoCliente buscarPorId(String id);
    RetornoCliente buscarPorEmail(String email);
    RetornoCliente buscarPorDocumento(String documento);
}
