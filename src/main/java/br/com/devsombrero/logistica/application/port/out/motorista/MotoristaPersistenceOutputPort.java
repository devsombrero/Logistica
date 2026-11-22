package br.com.devsombrero.logistica.application.port.out.motorista;

import br.com.devsombrero.logistica.application.port.out.crud.CrudPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.model.Motorista;

import java.util.Optional;

public interface MotoristaPersistenceOutputPort
    extends CrudPersistenceOutputPort<Motorista, String> {
    boolean existePorCpf(String cpf);
    boolean existePorCnh(String cnh);
    Optional<Motorista> buscarPorCnh(String cnh);
    Optional<Motorista> buscarPorCpf(String cpf);
    boolean existeCnhOuCpfEmOutroMotorista(String cnh, String cpf, String id);
}
