package br.com.devsombrero.logistica.domain.mapper;

import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Cliente;

public final class ClienteMapper {
    public static RetornoCliente toResponse(Cliente cliente) {
        return new RetornoCliente(
                cliente.id(),
                cliente.nome(),
                cliente.documento(),
                cliente.telefone(),
                cliente.email(),
                cliente.endereco(),
                cliente.cidade(),
                cliente.estado()
        );
    }
}
