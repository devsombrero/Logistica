package br.com.devsombrero.logistica.application.usecase.motorista;

import br.com.devsombrero.logistica.application.port.in.motorista.AtualizarMotoristaInputPort;
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
public class AtualizarMotoristaUseCase implements AtualizarMotoristaInputPort {

    private final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public AtualizarMotoristaUseCase(
            final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.motoristaPersistenceOutputPort = motoristaPersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public RetornoMotorista atualizar(String id, Motorista motorista) {
        logger.info(getClass(), "Iniciando atualização de motorista");

        if (motorista == null) {
            logger.warn(getClass(), "Atualização de motorista recusado: Motorista não pode ser null!");
            throw new RequisicaoInvalidaException("O motorista não pode ser nulo!");
        }

        if (motoristaPersistenceOutputPort
                .existeCnhOuCpfEmOutroMotorista(motorista.cnh(), motorista.cpf(), id)) {
            logger.warn(getClass(), "Atualização de motorista recusado: Outro motorista já existe com esse CNH/CPF");
            throw new RequisicaoInvalidaException(
                    "Já existe outro motorista cadastrado com essa CNH ou CPF!"
            );
        }

        if (motorista.validadeCnh() != null &&
                motorista.validadeCnh().isBefore(LocalDate.now())) {

            logger.warn(getClass(), "Atualização de motorista recusado: CNH vencida ou sem validade!");

            throw new EntidadeUnprocessableException(
                    "Não é possível cadastrar um motorista com a CNH vencida ou sem validade!"
            );
        }

        Motorista motoristaSalvo = this.motoristaPersistenceOutputPort.atualizar(id, motorista);

        logger.info(getClass(), "Motorista atualizado com sucesso");
        return MotoristaMapper.toResponse(motoristaSalvo);
    }
}
