package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;

public interface MotoristaService {
    RetornoMotorista cadastrar(Motorista motorista);
    RetornoMotorista buscarPorId(String id);
    RetornoMotorista buscarPorCnh(String cnh);
    RetornoMotorista buscarPorCpf(String cpf);
}
