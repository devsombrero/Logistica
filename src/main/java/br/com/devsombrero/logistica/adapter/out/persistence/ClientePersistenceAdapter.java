package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.application.port.out.ClientePersistenceOutputPort;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.devsombrero.logistica.adapter.out.persistence.ClienteDocument.fromDomain;
import static br.com.devsombrero.logistica.adapter.out.persistence.ClienteDocument.toDomain;

@Service
@RequiredArgsConstructor
public class ClientePersistenceAdapter implements ClientePersistenceOutputPort {

    private final ClienteMongoRepository repository;

    @Override
    public Paginado<Cliente> buscarPaginado(int tamanhoPagina, int paginaAtual) {

        Pageable pageable = PageRequest.of(paginaAtual, tamanhoPagina);

        Page<ClienteDocument> page = repository.findAll(pageable);

        return new Paginado<>(
                page.getContent().stream()
                        .map(ClienteDocument::toDomain)
                        .toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    public Optional<Cliente> buscarPorId(String id) {
        return repository.findById(id)
                .map(ClienteDocument::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .map(ClienteDocument::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorDocumento(String documento) {
        return repository.findByDocumento(documento)
                .map(ClienteDocument::toDomain);
    }

    @Override
    public boolean existePorEmailOuDocumento(String email, String documento) {
        return repository.existsByEmailOrDocumento(email, documento);
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteDocument document = fromDomain(cliente);
        return toDomain(repository.save(document));
    }

    @Override
    public Cliente atualizar(String id, Cliente cliente) {
        ClienteDocument document = repository.findById(id)
                .orElseThrow(() -> new EntidadeNotFoundException("Cliente não encontrado: " + id));
        ClienteDocument atualizado = document.atualizar(cliente);
        return toDomain(repository.save(atualizado));
    }

    @Override
    public void deletar(String id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNotFoundException(
                    "Cliente não encontrado: " + id
            );
        }

        repository.deleteById(id);

    }
}
