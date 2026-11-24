package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.veiculo.*;
import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeiculoServiceImpl implements VeiculoService{

    private final BuscarVeiculoPaginadoInputPort buscarVeiculoPaginadoInputPort;
    private final CadastrarVeiculoInputPort cadastrarVeiculoInputPort;
    private final BuscarVeiculoInputPort buscarVeiculoInputPort;
    private final AtualizarVeiculoInputPort atualizarVeiculoInputPort;
    private final ApagarVeiculoInputPort apagarVeiculoInputPort;

    @Override
    public Paginado<RetornoVeiculo> buscarVeiculoPaginado(
            int paginaAtual, int tamanhoPagina, FiltroPesquisaVeiculo filtro
    ) {
        return this.buscarVeiculoPaginadoInputPort.buscarPaginado(paginaAtual, tamanhoPagina, filtro);
    }

    @Override
    public RetornoVeiculo cadastrar(Veiculo veiculo) {
        return this.cadastrarVeiculoInputPort.cadastrar(veiculo);
    }

    @Override
    public RetornoVeiculo buscarPorId(String id) {
        return this.buscarVeiculoInputPort.buscarPorId(id);
    }

    @Override
    public RetornoVeiculo buscarPorPlaca(String placa) {
        return this.buscarVeiculoInputPort.buscarPorPlaca(placa);
    }

    @Override
    public RetornoVeiculo atualizar(String id, Veiculo veiculo) {
        return this.atualizarVeiculoInputPort.atualizar(id, veiculo);
    }

    @Override
    public void apagar(String id) {
        this.apagarVeiculoInputPort.apagar(id);
    }
}
