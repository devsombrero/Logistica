package br.com.devsombrero.logistica.adapter.out.persistence.motorista;

import br.com.devsombrero.logistica.application.port.out.motorista.MotoristaPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.domain.model.Paginado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.devsombrero.logistica.adapter.out.persistence.motorista.MotoristaDocument.fromDomain;
import static br.com.devsombrero.logistica.adapter.out.persistence.motorista.MotoristaDocument.toDomain;

@Service
@RequiredArgsConstructor
public class MotoristaPersistenceAdapter implements MotoristaPersistenceOutputPort {

    private final MotoristaMongoRepository repository;

    @Override
    public Paginado<Motorista> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        Pageable pageable = PageRequest.of(paginaAtual, tamanhoPagina);

        Page<MotoristaDocument> page = repository.findAll(pageable);

        return new Paginado<>(
                page.getContent().stream()
                        .map(MotoristaDocument::toDomain)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public Optional<Motorista> buscarPorId(String id) {
        return repository.findById(id)
                .map(MotoristaDocument::toDomain);
    }

    @Override
    public Motorista salvar(Motorista motorista) {
        MotoristaDocument document = fromDomain(motorista);
        return toDomain(repository.save(document));
    }

    @Override
    public Motorista atualizar(String id, Motorista motorista) {
        MotoristaDocument document = repository.findById(id)
                .orElseThrow(() -> new EntidadeNotFoundException("Motorista não encontrado: " + id));
        MotoristaDocument atualizado = document.atualizar(motorista);
        return toDomain(repository.save(atualizado));
    }

    @Override
    public void deletar(String id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNotFoundException(
                    "Motorista não encontrado: " + id
            );
        }

        repository.deleteById(id);

    }

    @Override
    public boolean existePorCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    @Override
    public boolean existePorCnh(String cnh) {
        return repository.existsByCnh(cnh);
    }

    @Override
    public Optional<Motorista> buscarPorCnh(String cnh) {
        return repository.findByCnh(cnh);
    }

    @Override
    public Optional<Motorista> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    @Override
    public boolean existeCnhOuCpfEmOutroMotorista(String cnh, String cpf, String id) {
        return repository.existeCnhOuCpfEmOutroMotorista(cnh, cpf, id);
    }
}
