package br.com.devsombrero.logistica.application.usecase.motorista;

import br.com.devsombrero.logistica.application.port.in.motorista.BuscarMotoristaPaginadoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.mapper.MotoristaMapper;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.domain.model.Paginado;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuscarMotoristaPaginadoUseCase implements BuscarMotoristaPaginadoInputPort {

    private final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public BuscarMotoristaPaginadoUseCase(
            final LoggerOutputPort logger,
            final MotoristaPersistenceOutputPort motoristaPersistenceOutputPort
    ) {
        this.logger = logger;
        this.motoristaPersistenceOutputPort = motoristaPersistenceOutputPort;
    }

    @Override
    public Paginado<RetornoMotorista> buscarPaginado(int paginaAtual, int tamanhoPagina) {
        logger.info(getClass(), "Iniciando busca paginada de motorista");

        if (tamanhoPagina <= 0 || paginaAtual < 0) {
            logger.warn(getClass(), "Parâmetros inválidos para busca paginada do motorista");

            throw new RequisicaoInvalidaException(
                    "O tamanho da página deve ser maior que zero e a página não pode ser negativa."
            );
        }

        Paginado<Motorista> resultado =
                motoristaPersistenceOutputPort.buscarPaginado(
                        tamanhoPagina,
                        paginaAtual
                );

        List<RetornoMotorista> motoristas = resultado.conteudo()
                .stream()
                .map(MotoristaMapper::toResponse)
                .toList();

        logger.info(getClass(), "Busca paginada de motoristas realizada com sucesso");

        return new Paginado<>(
                motoristas,
                resultado.paginaAtual(),
                resultado.tamanhoPagina(),
                resultado.totalPaginas(),
                resultado.totalElementos()
        );
    }
}
