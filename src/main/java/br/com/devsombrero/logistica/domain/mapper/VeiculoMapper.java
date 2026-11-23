package br.com.devsombrero.logistica.domain.mapper;

import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.model.Veiculo;

public final class VeiculoMapper {
    static RetornoVeiculo toResponse(Veiculo veiculo) {
        return new RetornoVeiculo(
                veiculo.id(),
                veiculo.placa(),
                veiculo.modelo(),
                veiculo.marca(),
                veiculo.capacidadeCarga(),
                veiculo.status()
        );
    }
}
