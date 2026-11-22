package br.com.devsombrero.logistica.application.port.in.motorista;

import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;

public interface BuscarMotoristaInputPort {
    RetornoMotorista buscarPorId(String id);
    RetornoMotorista buscarPorCnh(String cnh);
    RetornoMotorista buscarPorCpf(String cpf);
}
