package br.com.devsombrero.logistica.application.usecase.cliente;

import br.com.devsombrero.logistica.application.port.in.cliente.ApagarClienteInputPort;
import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ApagarClienteUseCase implements ApagarClienteInputPort {

    private final ClientePersistenceOutputPort clientePersistenceOutputPort;
    private final LoggerOutputPort logger;

    public ApagarClienteUseCase(
            final ClientePersistenceOutputPort clientePersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public void apagar(String id) {

        logger.info(getClass(), "Iniciando exclusão de cliente");

        clientePersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Cliente não encontrado para exclusão");

                    return new EntidadeNotFoundException(
                            "Cliente não encontrado com esse ID"
                    );
                });

        clientePersistenceOutputPort.deletar(id);

        logger.info(getClass(), "Cliente excluído com sucesso");
    }
}