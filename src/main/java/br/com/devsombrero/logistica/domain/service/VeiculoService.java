package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Veiculo;

public interface VeiculoService {
    Paginado<RetornoVeiculo> buscarVeiculoPaginado(
            int paginaAtual, int tamanhoPagina, FiltroPesquisaVeiculo filtro
    );
    RetornoVeiculo cadastrar(Veiculo veiculo);
    RetornoVeiculo buscarPorId(String id);
    RetornoVeiculo buscarPorPlaca(String placa);
    RetornoVeiculo atualizar(String id, Veiculo veiculo);
    void apagar(String id);
}
