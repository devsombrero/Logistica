package br.com.devsombrero.logistica.application.port.in.veiculo;

import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;

public interface CadastrarVeiculoInputPort {
    RetornoMotorista cadastrar(Motorista motorista);
}
