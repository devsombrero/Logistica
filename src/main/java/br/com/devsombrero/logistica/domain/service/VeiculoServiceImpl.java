package br.com.devsombrero.logistica.domain.service;

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
}
