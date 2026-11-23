package br.com.devsombrero.logistica.application.usecase.veiculo;

import br.com.devsombrero.logistica.application.port.in.veiculo.CadastrarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.mapper.VeiculoMapper;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import org.springframework.stereotype.Component;

@Component
public class CadastrarVeiculoUseCase implements CadastrarVeiculoInputPort {

    private final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public CadastrarVeiculoUseCase(
            final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.veiculoPersistenceOutputPort = veiculoPersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public RetornoVeiculo cadastrar(Veiculo veiculo) {
        logger.info(getClass(), "Iniciando cadastro de veículo");

        if (veiculo == null) {
            logger.warn(getClass(), "Cadastro de veiculo recusado: O veiculo não pode ser 'null'");
            throw new RequisicaoInvalidaException("O veículo não pode ser nulo.");
        }

        if (veiculoPersistenceOutputPort.existePorPlaca(veiculo.placa())) {
            logger.warn(getClass(), "Cadastro de veiculo recusado: Veículo já tem cadastro com a placa informada");
            throw new EntidadeUnprocessableException("Já existe um veículo cadastrado com essa placa.");
        }

        logger.info(getClass(), "Cadastro de veiculo realizado com sucesso");
        return VeiculoMapper.toResponse(this.veiculoPersistenceOutputPort.salvar(veiculo));
    }
}
