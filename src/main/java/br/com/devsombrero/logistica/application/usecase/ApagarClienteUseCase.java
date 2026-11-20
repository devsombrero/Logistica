package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.in.ApagarClienteInputPort;
import br.com.devsombrero.logistica.application.port.out.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.LoggerOutputPort;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApagarClienteUseCase implements ApagarClienteInputPort {

    private final ClientePersistenceOutputPort clientePersistenceOutputPort;
    private final LoggerOutputPort logger;

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