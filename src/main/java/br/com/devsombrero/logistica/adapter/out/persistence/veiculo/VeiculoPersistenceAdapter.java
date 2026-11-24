package br.com.devsombrero.logistica.adapter.out.persistence.veiculo;

import br.com.devsombrero.logistica.application.port.out.veiculo.VeiculoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VeiculoPersistenceAdapter implements VeiculoPersistenceOutputPort {

    private final VeiculoMongoRepository repository;
    private final MongoTemplate mongoTemplate;

    @Override
    public boolean existePorPlaca(String placa) {
        return this.repository.existsByPlaca(placa);
    }

    @Override
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return repository.findByPlaca(placa);
    }

    @Override
    public boolean existePorPlacaIdDiferente(String placa, String id) {
        return repository.existePlacaIdDiferente(placa, id);
    }

    @Override
    public Paginado<Veiculo> buscarPaginadoComFiltros(
            int paginaAtual,
            int tamanhoPagina,
            FiltroPesquisaVeiculo filtro
    ) {
        List<Criteria> criterios = new ArrayList<>();

        Optional.ofNullable(filtro.modelo())
                .filter(modelo -> !modelo.isBlank())
                .ifPresent(modelo -> criterios.add(
                        Criteria.where("modelo").regex(modelo, "i")
                ));

        Optional.ofNullable(filtro.marca())
                .filter(marca -> !marca.isBlank())
                .ifPresent(marca -> criterios.add(
                        Criteria.where("marca").regex(marca, "i")
                ));

        Optional.ofNullable(filtro.capacidadeCarga())
                .ifPresent(capacidade -> criterios.add(
                        Criteria.where("capacidadeCarga").is(capacidade)
                ));

        Optional.ofNullable(filtro.status())
                .ifPresent(status -> criterios.add(
                        Criteria.where("status").is(status)
                ));

        Query query = new Query();

        Optional.of(criterios)
                .filter(lista -> !lista.isEmpty())
                .ifPresent(lista -> query.addCriteria(
                        new Criteria().andOperator(lista.toArray(new Criteria[0]))
                ));

        long totalElementos = mongoTemplate.count(query, VeiculoDocument.class);

        query.with(PageRequest.of(paginaAtual, tamanhoPagina));

        List<Veiculo> veiculos = mongoTemplate.find(
                        query,
                        VeiculoDocument.class
                )
                .stream()
                .map(VeiculoDocument::toDomain)
                .toList();

        int totalPaginas = (int) Math.ceil(
                (double) totalElementos / tamanhoPagina
        );

        return new Paginado<>(
                veiculos,
                paginaAtual,
                tamanhoPagina,
                totalPaginas,
                totalElementos
        );
    }

    @Override
    public Paginado<Veiculo> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        Pageable pageable = PageRequest.of(paginaAtual, tamanhoPagina);

        Page<VeiculoDocument> page = repository.findAll(pageable);

        return new Paginado<>(
                page.getContent().stream()
                        .map(VeiculoDocument::toDomain)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public Optional<Veiculo> buscarPorId(String id) {
        return this.repository.findById(id)
                .map(VeiculoDocument::toDomain);
    }

    @Override
    public Veiculo salvar(Veiculo entidade) {
        VeiculoDocument document = VeiculoDocument.fromDomain(entidade);
        return VeiculoDocument.toDomain(repository.save(document));
    }

    @Override
    public Veiculo atualizar(String id, Veiculo entidade) {
        VeiculoDocument document = repository.findById(id)
                .orElseThrow(() -> new EntidadeNotFoundException("Veículo não encontrado"));
        VeiculoDocument atualizado = document.atualizar(entidade);
        return VeiculoDocument.toDomain(repository.save(atualizado));
    }

    @Override
    public void deletar(String id) {
        if(!repository.existsById(id)) {
            throw new EntidadeNotFoundException("Veículo não encontrado");
        }

        repository.deleteById(id);
    }
}
