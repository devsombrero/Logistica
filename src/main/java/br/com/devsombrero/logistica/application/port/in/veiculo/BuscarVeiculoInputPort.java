package br.com.devsombrero.logistica.application.port.in.veiculo;

import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;

public interface BuscarVeiculoInputPort {
    RetornoVeiculo buscarPorId(String id);
    RetornoVeiculo buscarPorPlaca(String placa);
}
