package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.in.BuscarClienteInputPort;
import br.com.devsombrero.logistica.application.port.out.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.mapper.ClienteMapper;
import br.com.devsombrero.logistica.domain.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class BuscarClienteUseCase implements BuscarClienteInputPort {

    private final ClientePersistenceOutputPort clientePersistenceOutputPort;

    public BuscarClienteUseCase(
            final ClientePersistenceOutputPort clientePersistenceOutputPort
    ) {
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
    }

    @Override
    public RetornoCliente buscarPorId(String id) {
        Cliente cliente = this.clientePersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNotFoundException("Cliente não encontrado com esse ID"));
        return ClienteMapper.toResponse(cliente);
    }

    @Override
    public RetornoCliente buscarPorEmail(String email) {
        Cliente cliente = this.clientePersistenceOutputPort.buscarPorEmail(email)
                .orElseThrow(() -> new EntidadeNotFoundException("Cliente não encontrado com esse E-mail"));
        return ClienteMapper.toResponse(cliente);
    }

    @Override
    public RetornoCliente buscarPorDocumento(String documento) {
        Cliente cliente = this.clientePersistenceOutputPort.buscarPorDocumento(documento)
                .orElseThrow(() -> new EntidadeNotFoundException("Cliente não encontrado com esse Documento"));
        return ClienteMapper.toResponse(cliente);
    }
}
