package br.com.devsombrero.logistica.application.port.out.produto;

import br.com.devsombrero.logistica.application.port.out.crud.CrudPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.model.Produto;

public interface ProdutoPersistenceOutputPort
        extends CrudPersistenceOutputPort<Produto, String> {
    boolean existePorSku(String sku);
}
