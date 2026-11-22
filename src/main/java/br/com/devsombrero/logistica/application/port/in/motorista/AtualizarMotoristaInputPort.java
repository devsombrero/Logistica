package br.com.devsombrero.logistica.application.port.in.motorista;

import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;

public interface AtualizarMotoristaInputPort {
    RetornoMotorista atualizar(String id, Motorista motorista);
}
