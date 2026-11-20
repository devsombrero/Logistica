package br.com.devsombrero.logistica.application.port.out.produto;

import br.com.devsombrero.logistica.application.port.out.crud.CrudPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.model.Produto;

import java.util.Optional;

public interface ProdutoPersistenceOutputPort
        extends CrudPersistenceOutputPort<Produto, String> {
    Optional<Produto> buscarPorSku(String id);
    boolean existePorSku(String sku);
}
