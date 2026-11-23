package br.com.devsombrero.logistica.application.port.in.veiculo;

import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.model.Veiculo;

public interface CadastrarVeiculoInputPort {
    RetornoVeiculo cadastrar(Veiculo veiculo);
}
