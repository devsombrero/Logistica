package br.com.devsombrero.logistica.application.port.out.cliente;

import br.com.devsombrero.logistica.application.port.out.crud.CrudPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.model.Cliente;

import java.util.Optional;

public interface ClientePersistenceOutputPort
    extends CrudPersistenceOutputPort<Cliente, String> {
    Optional<Cliente> buscarPorEmail(String email);
    Optional<Cliente> buscarPorDocumento(String documento);
    boolean existePorEmailOuDocumento(String email, String documento);
    boolean existeEmailOuDocumentoEmOutroCliente(String email, String documento, String id);
    void deletar(String id);
}
