package br.com.devsombrero.logistica.adapter.out.persistence.produto;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProdutoMongoRepository extends MongoRepository<ProdutoDocument, String> {
    boolean existsBySku(String sku);
}
