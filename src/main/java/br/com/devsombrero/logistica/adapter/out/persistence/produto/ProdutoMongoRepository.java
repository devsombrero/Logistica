package br.com.devsombrero.logistica.adapter.out.persistence.produto;

import br.com.devsombrero.logistica.domain.model.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProdutoMongoRepository extends MongoRepository<ProdutoDocument, String> {
    boolean existsBySku(String sku);
    Optional<Produto> findBySku(String id);
}
