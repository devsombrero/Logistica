package br.com.devsombrero.logistica.application.usecase.veiculo;

import br.com.devsombrero.logistica.application.port.in.veiculo.BuscarVeiculoPaginadoInputPort;
import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.mapper.VeiculoMapper;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuscarVeiculoPaginadoUseCase implements BuscarVeiculoPaginadoInputPort {

    private final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort;
    private final LoggerOutputPort logger;

    public BuscarVeiculoPaginadoUseCase(
            final VeiculoPersistenceOutputPort veiculoPersistenceOutputPort,
            final LoggerOutputPort logger
    ) {
        this.veiculoPersistenceOutputPort = veiculoPersistenceOutputPort;
        this.logger = logger;
    }

    @Override
    public Paginado<RetornoVeiculo> buscarPaginado(
            int paginaAtual,
            int tamanhoPagina,
            FiltroPesquisaVeiculo filtroPesquisaVeiculo
    ) {

        logger.info(getClass(), "Iniciando busca paginada de veículos");

        if (tamanhoPagina <= 0 || paginaAtual < 0) {
            logger.warn(getClass(), "Parâmetros inválidos para busca paginada de veículos");

            throw new RequisicaoInvalidaException(
                    "O tamanho da página deve ser maior que zero e a página não pode ser negativa."
            );
        }

        Paginado<Veiculo> resultado;

        if (filtroPesquisaVeiculo == null) {
            resultado = veiculoPersistenceOutputPort.buscarPaginado(
                    tamanhoPagina,
                    paginaAtual
            );
        } else {
            resultado = veiculoPersistenceOutputPort.buscarPaginadoComFiltros(
                    paginaAtual,
                    tamanhoPagina,
                    filtroPesquisaVeiculo
            );
        }

        List<RetornoVeiculo> veiculos = resultado.conteudo()
                .stream()
                .map(VeiculoMapper::toResponse)
                .toList();

        logger.info(getClass(), "Busca paginada de veículos realizada com sucesso");

        return new Paginado<>(
                veiculos,
                resultado.paginaAtual(),
                resultado.tamanhoPagina(),
                resultado.totalPaginas(),
                resultado.totalElementos()
        );
    }
}