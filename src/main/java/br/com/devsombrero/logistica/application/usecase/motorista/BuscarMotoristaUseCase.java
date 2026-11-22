package br.com.devsombrero.logistica.application.usecase.motorista;

import br.com.devsombrero.logistica.application.port.in.motorista.BuscarMotoristaInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.mapper.MotoristaMapper;
import org.springframework.stereotype.Component;

@Component
public class BuscarMotoristaUseCase implements BuscarMotoristaInputPort {

    private final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public BuscarMotoristaUseCase(
            final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.motoristaPersistenceOutputPort = motoristaPersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public RetornoMotorista buscarPorId(String id) {
        logger.info(getClass(),"Iniciando busca do motorista por id");
        return this.motoristaPersistenceOutputPort.buscarPorId(id)
                .map(MotoristaMapper::toResponse)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Motorista não encontrado com esse id");
                    return new EntidadeNotFoundException("Motorista não encontrado!");
                });
    }

    @Override
    public RetornoMotorista buscarPorCnh(String cnh) {
        logger.info(getClass(),"Iniciando busca do motorista por cnh");
        return this.motoristaPersistenceOutputPort.buscarPorCnh(cnh)
                .map(MotoristaMapper::toResponse)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Motorista não encontrado com esse cnh");
                    return new EntidadeNotFoundException("Motorista não encontrado!");
                });
    }

    @Override
    public RetornoMotorista buscarPorCpf(String cpf) {
        logger.info(getClass(),"Iniciando busca do motorista por cpf");
        return this.motoristaPersistenceOutputPort.buscarPorCpf(cpf)
                .map(MotoristaMapper::toResponse)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Motorista não encontrado com esse cpf");
                    return new EntidadeNotFoundException("Motorista não encontrado!");
                });
    }
}
