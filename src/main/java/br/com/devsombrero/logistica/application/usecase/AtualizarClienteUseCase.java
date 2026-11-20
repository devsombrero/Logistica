package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.in.AtualizarClienteInputPort;
import br.com.devsombrero.logistica.application.port.out.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.LoggerOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.mapper.ClienteMapper;
import br.com.devsombrero.logistica.domain.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class AtualizarClienteUseCase implements AtualizarClienteInputPort {

    private final ClientePersistenceOutputPort clientePersistenceOutputPort;
    private final LoggerOutputPort logger;

    public AtualizarClienteUseCase(
            final ClientePersistenceOutputPort clientePersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public RetornoCliente atualizar(String id, Cliente cliente) {

        logger.info(getClass(), "Iniciando atualização de cliente");

        clientePersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Cliente não encontrado para atualização");

                    return new EntidadeNotFoundException(
                            "Cliente não encontrado: " + id
                    );
                });

        if (clientePersistenceOutputPort.existeEmailOuDocumentoEmOutroCliente(
                cliente.email(),
                cliente.documento(),
                id
        )) {
            logger.warn(getClass(), "Atualização de cliente recusada: e-mail ou documento já cadastrado");

            throw new EntidadeUnprocessableException(
                    "O e-mail ou documento informado já pertence a outro cliente."
            );
        }

        Cliente clienteAtualizado = clientePersistenceOutputPort.atualizar(id, cliente);

        logger.info(getClass(), "Cliente atualizado com sucesso");

        return ClienteMapper.toResponse(clienteAtualizado);
    }
}