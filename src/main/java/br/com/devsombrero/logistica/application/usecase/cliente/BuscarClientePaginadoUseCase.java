package br.com.devsombrero.logistica.application.usecase.cliente;

import br.com.devsombrero.logistica.application.port.in.cliente.BuscarClientePaginadoInputPort;
import br.com.devsombrero.logistica.application.port.out.cliente.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.mapper.ClienteMapper;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuscarClientePaginadoUseCase implements BuscarClientePaginadoInputPort {

    private final ClientePersistenceOutputPort clientePersistenceOutputPort;
    private final LoggerOutputPort logger;

    public BuscarClientePaginadoUseCase(
            final ClientePersistenceOutputPort clientePersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.clientePersistenceOutputPort = clientePersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public Paginado<RetornoCliente> buscarPaginado(
            int tamanhoPagina,
            int paginaAtual
    ) {

        logger.info(getClass(), "Iniciando busca paginada de clientes");

        if (tamanhoPagina <= 0 || paginaAtual < 0) {
            logger.warn(getClass(), "Parâmetros inválidos para busca paginada");

            throw new RequisicaoInvalidaException(
                    "O tamanho da página deve ser maior que zero e a página não pode ser negativa."
            );
        }

        Paginado<Cliente> resultado =
                clientePersistenceOutputPort.buscarPaginado(
                        tamanhoPagina,
                        paginaAtual
                );

        List<RetornoCliente> clientes = resultado.conteudo()
                .stream()
                .map(ClienteMapper::toResponse)
                .toList();

        logger.info(getClass(), "Busca paginada de clientes realizada com sucesso");

        return new Paginado<>(
                clientes,
                resultado.paginaAtual(),
                resultado.tamanhoPagina(),
                resultado.totalPaginas(),
                resultado.totalElementos()
        );
    }
}