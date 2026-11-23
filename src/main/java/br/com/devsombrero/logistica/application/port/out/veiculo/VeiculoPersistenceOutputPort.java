package br.com.devsombrero.logistica.application.port.out.veiculo;

import br.com.devsombrero.logistica.application.port.out.crud.CrudPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.model.Veiculo;

public interface VeiculoPersistenceOutputPort
    extends CrudPersistenceOutputPort<Veiculo, String> {
    boolean existePorPlaca(String placa);
}
