package br.com.devsombrero.logistica.application.port.out.motorista;

import br.com.devsombrero.logistica.application.port.out.crud.CrudPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.model.Motorista;

public interface MotoristaPersistenceOutputPort
    extends CrudPersistenceOutputPort<Motorista, String> {
    boolean existePorCpf(String cpf);
    boolean existePorCnh(String cnh);
}
