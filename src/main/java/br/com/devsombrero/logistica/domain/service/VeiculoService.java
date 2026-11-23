package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.model.Veiculo;

public interface VeiculoService {
    RetornoVeiculo cadastrar(Veiculo veiculo);
    RetornoVeiculo buscarPorId(String id);
    RetornoVeiculo buscarPorPlaca(String placa);
}
