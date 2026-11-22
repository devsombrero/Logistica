package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.domain.model.Paginado;

public interface MotoristaService {
    RetornoMotorista cadastrar(Motorista motorista);
    RetornoMotorista buscarPorId(String id);
    RetornoMotorista buscarPorCnh(String cnh);
    RetornoMotorista buscarPorCpf(String cpf);
    RetornoMotorista atualizar(String id, Motorista motorista);
    Paginado<RetornoMotorista> buscarPaginado(int paginaAtual, int tamanhoPagina);
    void apagar(String id);
}
