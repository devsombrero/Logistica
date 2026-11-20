package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.in.AtualizarClienteInputPort;
import br.com.devsombrero.logistica.application.port.out.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.mapper.ClienteMapper;
import br.com.devsombrero.logistica.domain.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class AtualizarClienteUseCase implements AtualizarClienteInputPort {

    private final ClientePersistenceOutputPort clientePersistenceOutputPort;

    public AtualizarClienteUseCase(
            final ClientePersistenceOutputPort clientePersistenceOutputPort
    ) {
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
    }

    @Override
    public RetornoCliente atualizar(String id, Cliente cliente) {
        clientePersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNotFoundException(
                        "Cliente não encontrado: " + id
                ));


        if (clientePersistenceOutputPort.existeEmailOuDocumentoEmOutroCliente(
                cliente.email(),
                cliente.documento(),
                id
        )) {
            throw new EntidadeUnprocessableException(
                    "O e-mail ou documento informado já pertence a outro cliente."
            );
        }

        return ClienteMapper.toResponse(this.clientePersistenceOutputPort.atualizar(id, cliente));
    }
}
