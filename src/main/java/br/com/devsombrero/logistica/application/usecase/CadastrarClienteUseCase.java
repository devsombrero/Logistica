package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.in.CadastrarClienteInputPort;
import br.com.devsombrero.logistica.application.port.out.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.LoggerOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.mapper.ClienteMapper;
import br.com.devsombrero.logistica.domain.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class CadastrarClienteUseCase implements CadastrarClienteInputPort {

    private final ClientePersistenceOutputPort clientePersistenceOutputPort;
    private final LoggerOutputPort logger;

    public CadastrarClienteUseCase(
            final ClientePersistenceOutputPort clientePersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public RetornoCliente cadastrar(Cliente cliente) {

        logger.info(getClass(), "Iniciando cadastro de cliente");

        if (cliente == null) {
            logger.warn(getClass(), "Cadastro de cliente recusado: requisição nula");

            throw new RequisicaoInvalidaException(
                    "Cliente não pode ser nulo"
            );
        }

        if (clientePersistenceOutputPort.existePorEmailOuDocumento(
                cliente.email(), cliente.documento()
        )) {
            logger.warn(getClass(), "Cadastro de cliente recusado: e-mail ou documento já cadastrado");

            throw new EntidadeUnprocessableException(
                    "Cliente já existe com esse e-mail/documento"
            );
        }

        Cliente clienteSalvo = clientePersistenceOutputPort.salvar(cliente);

        logger.info(getClass(), "Cliente cadastrado com sucesso");

        return ClienteMapper.toResponse(clienteSalvo);
    }
}