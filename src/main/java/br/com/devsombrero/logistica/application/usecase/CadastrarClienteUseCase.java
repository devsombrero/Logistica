package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.in.CadastrarClienteInputPort;
import br.com.devsombrero.logistica.application.port.out.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.mapper.ClienteMapper;
import br.com.devsombrero.logistica.domain.model.Cliente;
import org.springframework.stereotype.Component;


@Component
public class CadastrarClienteUseCase implements CadastrarClienteInputPort {

    private final ClientePersistenceOutputPort clientePersistenceOutputPort;

    public CadastrarClienteUseCase(
            final ClientePersistenceOutputPort clientePersistenceOutputPort
    ) {
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
    }

    @Override
    public RetornoCliente cadastrar(Cliente cliente) {
        if (cliente == null) {
            throw new RequisicaoInvalidaException("Cliente não pode ser nulo");
        }

        if (clientePersistenceOutputPort
                .existePorEmailOuDocumento(cliente.email(), cliente.documento())
        ) {
            throw new EntidadeUnprocessableException("Cliente já existe com esse e-mail/documento");
        }

        return ClienteMapper.toResponse(clientePersistenceOutputPort.salvar(cliente));
    }
}
