package br.com.devsombrero.logistica.application.usecase.veiculo;

import br.com.devsombrero.logistica.application.port.in.veiculo.AtualizarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.mapper.VeiculoMapper;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import org.springframework.stereotype.Component;

@Component
public class AtualizarVeiculoUseCase implements AtualizarVeiculoInputPort {

    private final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public AtualizarVeiculoUseCase(
            final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.veiculoPersistenceOutputPort = veiculoPersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public RetornoVeiculo atualizar(String id, Veiculo veiculo) {
        logger.info(getClass(), "Iniciando atualização do veiculo");

        this.veiculoPersistenceOutputPort.buscarPorId(id)
                .orElseThrow(() -> {
                    logger.warn(getClass(), "Veiculo não encontrado para atualização");
                    return new EntidadeNotFoundException("Veiculo não encontrado");
                });

        if(this.veiculoPersistenceOutputPort.existePorPlacaIdDiferente(veiculo.placa(), id)) {
            logger.warn(getClass(), "Já existe um outro veiculo cadastrado com essa placa!");
            throw new EntidadeUnprocessableException("Já existe um veiculo cadastrado com essa placa");
        }

        var atualizado = this.veiculoPersistenceOutputPort.atualizar(id, veiculo);
        logger.info(getClass(), "Veiculo atualizado com sucesso");
        return VeiculoMapper.toResponse(atualizado);
    }
}
