package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.veiculo.ApagarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.in.veiculo.AtualizarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.in.veiculo.BuscarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.in.veiculo.CadastrarVeiculoInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeiculoServiceImpl implements VeiculoService{

    private final CadastrarVeiculoInputPort cadastrarVeiculoInputPort;
    private final BuscarVeiculoInputPort buscarVeiculoInputPort;
    private final AtualizarVeiculoInputPort atualizarVeiculoInputPort;
    private final ApagarVeiculoInputPort apagarVeiculoInputPort;

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
