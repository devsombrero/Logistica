package br.com.devsombrero.logistica.application.port.in.cliente;

import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Cliente;

public interface CadastrarClienteInputPort {
    RetornoCliente cadastrar(Cliente cliente);
}
