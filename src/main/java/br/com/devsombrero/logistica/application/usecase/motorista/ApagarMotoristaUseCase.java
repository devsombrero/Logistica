package br.com.devsombrero.logistica.application.usecase.motorista;

import br.com.devsombrero.logistica.application.port.in.motorista.ApagarMotoristaInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ApagarMotoristaUseCase implements ApagarMotoristaInputPort {

    private final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public ApagarMotoristaUseCase(
            final LoggerOutputPort logger,
            final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort
    ) {
        this.logger = logger;
        this.motoristaPersistenceOutputPort = motoristaPersistenceOutputPort;
    }

    @Override
    public void apagar(String id) {
        logger.info(getClass(), "Iniciando deleção do motorista");
        this.motoristaPersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Motorista não encontrado com esse ID");
                    return new EntidadeNotFoundException("Motorista não encontrado com esse ID");
                });
        logger.info(getClass(), "Motorista apagado com sucesso");
        this.motoristaPersistenceOutputPort.deletar(id);
    }
}
