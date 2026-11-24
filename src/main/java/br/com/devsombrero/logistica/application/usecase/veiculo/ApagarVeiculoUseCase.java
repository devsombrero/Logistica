package br.com.devsombrero.logistica.application.usecase.veiculo;

import br.com.devsombrero.logistica.application.port.in.veiculo.ApagarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ApagarVeiculoUseCase implements ApagarVeiculoInputPort {

    private final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public ApagarVeiculoUseCase(
            final LoggerOutputPort logger,
            final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort
    ) {
        this.logger = logger;
        this.veiculoPersistenceOutputPort = veiculoPersistenceOutputPort;
    }

    @Override
    public void apagar(String id) {
        logger.info(getClass(), "Iniciando exclusão do veiculo");

        this.veiculoPersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Veiculo não encontrado com esse ID");
                    return new EntidadeNotFoundException("Veiculo não encontrado");
                });

        this.veiculoPersistenceOutputPort.deletar(id);
        logger.info(getClass(), "Exclusão sucedida do veiculo");
    }
}
