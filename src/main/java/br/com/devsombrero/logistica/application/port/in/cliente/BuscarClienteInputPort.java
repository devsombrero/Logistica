package br.com.devsombrero.logistica.application.port.in.cliente;

import br.com.devsombrero.logistica.domain.dto.RetornoCliente;

public interface BuscarClienteInputPort {
    RetornoCliente buscarPorId(String id);
    RetornoCliente buscarPorEmail(String email);
    RetornoCliente buscarPorDocumento(String documento);
}
