package br.com.devsombrero.logistica.application.usecase.veiculo;

import br.com.devsombrero.logistica.application.port.in.veiculo.BuscarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.mapper.VeiculoMapper;
import org.springframework.stereotype.Component;

@Component
public class BuscarVeiculoUseCase implements BuscarVeiculoInputPort {

    private final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort;

    private final LoggerOutputPort logger;

    public BuscarVeiculoUseCase(
            final LoggerOutputPort logger,
            final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort
    ) {
        this.logger = logger;
        this.veiculoPersistenceOutputPort = veiculoPersistenceOutputPort;
    }

    @Override
    public RetornoVeiculo buscarPorId(String id) {
        logger.info(getClass(), "Iniciando de consulta de veículo por ID");
        var veiculo = this.veiculoPersistenceOutputPort.buscarPorId(id)
                .map(VeiculoMapper::toResponse)
                .orElseThrow(() -> {
                    logger.info(getClass(), "Consulta de veículo não encontrado com esse ID");
                    return new EntidadeNotFoundException("Veiculo não encontrado com o ID informado");
                });

        logger.info(getClass(), "Consulta de veiculo por ID realizada com sucesso");
        return veiculo;
    }

    @Override
    public RetornoVeiculo buscarPorPlaca(String placa) {
        logger.info(getClass(), "Iniciando de consulta de veículo por placa");
        var veiculo = this.veiculoPersistenceOutputPort.buscarPorPlaca(placa)
                .map(VeiculoMapper::toResponse)
                .orElseThrow(() -> {
                    logger.info(getClass(), "Consulta de veículo não encontrado com essa placa");
                    return new EntidadeNotFoundException("Veiculo não encontrado com o placa informada");
                });

        logger.info(getClass(), "Consulta de veiculo por placa realizada com sucesso");
        return veiculo;
    }
}
