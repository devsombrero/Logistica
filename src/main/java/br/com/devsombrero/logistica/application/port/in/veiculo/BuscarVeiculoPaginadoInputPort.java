package br.com.devsombrero.logistica.application.port.in.veiculo;

import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.model.Paginado;

public interface BuscarVeiculoPaginadoInputPort {
    Paginado<RetornoVeiculo> buscarPaginado(
            int paginaAtual,
            int tamanhoPagina,
            FiltroPesquisaVeiculo filtroPesquisaVeiculo
    );
}
