package br.com.devsombrero.logistica.application.port.out;

import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;

import java.util.Optional;

public interface ClientePersistenceOutputPort {
    Paginado<Cliente> buscarPaginado(int tamanhoPagina, int paginaAtual);
    Optional<Cliente> buscarPorId(String id);
    boolean existePorEmailOuDocumento(String email, String documento);
    Cliente salvar(Cliente cliente);
    Cliente atualizar(String id, Cliente cliente);
    void deletar(String id);
}
