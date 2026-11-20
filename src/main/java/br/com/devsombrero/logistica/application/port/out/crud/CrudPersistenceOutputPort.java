package br.com.devsombrero.logistica.application.port.out.crud;

import br.com.devsombrero.logistica.domain.model.Paginado;

import java.util.Optional;

public interface CrudPersistenceOutputPort<T, ID> {
    Paginado<T> buscarPaginado(int tamanhoPagina, int paginaAtual);
    Optional<T> buscarPorId(ID id);
    T salvar(T entidade);
    T atualizar(ID id, T entidade);
    void deletar(ID id);
}
