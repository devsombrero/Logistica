package br.com.devsombrero.logistica.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClienteMongoRepository extends MongoRepository<ClienteDocument, String> {
    boolean existsByEmailOrDocumento(String email, String documento);
}
