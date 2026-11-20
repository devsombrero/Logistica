package br.com.devsombrero.logistica.application.usecase.cliente;

import br.com.devsombrero.logistica.application.port.in.cliente.BuscarClienteInputPort;
import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.mapper.ClienteMapper;
import br.com.devsombrero.logistica.domain.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class BuscarClienteUseCase implements BuscarClienteInputPort {

    private final ClientePersistenceOutputPort clientePersistenceOutputPort;
    private final LoggerOutputPort logger;

    public BuscarClienteUseCase(
            final ClientePersistenceOutputPort clientePersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public RetornoCliente buscarPorId(String id) {
        logger.info(getClass(), "Iniciando busca de cliente por ID");

        Cliente cliente = clientePersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Cliente não encontrado na busca por ID");
                    return new EntidadeNotFoundException(
                            "Cliente não encontrado com esse ID"
                    );
                });

        logger.info(getClass(), "Cliente encontrado com sucesso por ID");

        return ClienteMapper.toResponse(cliente);
    }

    @Override
    public RetornoCliente buscarPorEmail(String email) {
        logger.info(getClass(), "Iniciando busca de cliente por e-mail");

        Cliente cliente = clientePersistenceOutputPort.buscarPorEmail(email)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Cliente não encontrado na busca por e-mail");
                    return new EntidadeNotFoundException(
                            "Cliente não encontrado com esse e-mail"
                    );
                });

        logger.info(getClass(), "Cliente encontrado com sucesso por e-mail");

        return ClienteMapper.toResponse(cliente);
    }

    @Override
    public RetornoCliente buscarPorDocumento(String documento) {
        logger.info(getClass(), "Iniciando busca de cliente por documento");

        Cliente cliente = clientePersistenceOutputPort.buscarPorDocumento(documento)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Cliente não encontrado na busca por documento");
                    return new EntidadeNotFoundException(
                            "Cliente não encontrado com esse documento"
                    );
                });

        logger.info(getClass(), "Cliente encontrado com sucesso por documento");

        return ClienteMapper.toResponse(cliente);
    }
}