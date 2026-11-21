package br.com.devsombrero.logistica.application.usecase.motorista;

import br.com.devsombrero.logistica.application.port.in.motorista.CadastrarMotoristaInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.mapper.MotoristaMapper;
import br.com.devsombrero.logistica.domain.model.Motorista;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CadastrarMotoristaUseCase implements CadastrarMotoristaInputPort {

    private final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public CadastrarMotoristaUseCase(
            final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.motoristaPersistenceOutputPort = motoristaPersistenceOutputPort;
        this.logger = logger;
    }


    @Override
    public RetornoMotorista cadastrar(Motorista motorista) {
        logger.info(getClass(), "Iniciando cadastro de motorista");

        if (motorista == null) {
            logger.warn(getClass(), "Cadastro de motorista recusado: Motorista não pode ser null!");
            throw new RequisicaoInvalidaException("O motorista não pode ser nulo!");
        }

        if (motoristaPersistenceOutputPort.existePorCpf(motorista.cpf())) {
            logger.warn(getClass(), "Cadastro de motorista recusado: Motorista com esse CPF já existe!");
            throw new EntidadeUnprocessableException("O motorista com esse cpf já existe na base de dados!");
        }

        if (motoristaPersistenceOutputPort.existePorCnh(motorista.cnh())) {
            logger.warn(getClass(), "Cadastro de motorista recusado: Motorista com esse CNH já existe!");
            throw new EntidadeUnprocessableException("O motorista com esse CNH já existe na base de dados!");
        }

        if (motorista.validadeCnh() == null ||
                motorista.validadeCnh().isBefore(LocalDate.now())) {

            logger.warn(getClass(), "Cadastro de motorista recusado: CNH vencida ou sem validade!");

            throw new EntidadeUnprocessableException(
                    "Não é possível cadastrar um motorista com a CNH vencida ou sem validade!"
            );
        }

        Motorista motoristaSalvo = this.motoristaPersistenceOutputPort.salvar(motorista);

        logger.info(getClass(), "Motorista cadastrado com sucesso");
        return MotoristaMapper.toResponse(motoristaSalvo);
    }
}
