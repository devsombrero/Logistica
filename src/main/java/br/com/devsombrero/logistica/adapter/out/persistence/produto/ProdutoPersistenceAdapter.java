package br.com.devsombrero.logistica.adapter.out.persistence.produto;

import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.devsombrero.logistica.adapter.out.persistence.produto.ProdutoDocument.fromDomain;
import static br.com.devsombrero.logistica.adapter.out.persistence.produto.ProdutoDocument.toDomain;

@Service
@RequiredArgsConstructor
public class ProdutoPersistenceAdapter implements ProdutoPersistenceOutputPort {

    private final ProdutoMongoRepository repository;

    @Override
    public Optional<Produto> buscarPorSku(String id) {
        return repository.findBySku(id);
    }

    @Override
    public boolean existePorSku(String sku) {
        return repository.existsBySku(sku);
    }

    @Override
    public boolean existePorSkuEIdDiferente(String sku, String id) {
        return repository.existePorSkuEIdDiferente(sku, id);
    }

    @Override
    public Paginado<Produto> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        Pageable pageable = PageRequest.of(paginaAtual, tamanhoPagina);

        Page<ProdutoDocument> page = repository.findAll(pageable);

        return new Paginado<>(
                page.getContent().stream()
                        .map(ProdutoDocument::toDomain)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public Optional<Produto> buscarPorId(String id) {
        return repository.findById(id)
                .map(ProdutoDocument::toDomain);
    }

    @Override
    public Produto salvar(Produto produto) {
        ProdutoDocument document = fromDomain(produto);
        return toDomain(repository.save(document));
    }

    @Override
    public Produto atualizar(String id, Produto produto) {
        ProdutoDocument document = repository.findById(id)
                .orElseThrow(() -> new EntidadeNotFoundException("Produto não encontrado: " + id));
        ProdutoDocument atualizado = document.atualizar(produto);
        return ProdutoDocument.toDomain(repository.save(atualizado));
    }

    @Override
    public void deletar(String id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNotFoundException(
                    "Produto não encontrado: " + id
            );
        }

        repository.deleteById(id);
    }
}
