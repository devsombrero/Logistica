package br.com.devsombrero.logistica.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClienteMongoRepository extends MongoRepository<ClienteDocument, String> {
    boolean existsByEmailOrDocumento(String email, String documento);
    Optional<ClienteDocument> findByEmail(String email);
    Optional<ClienteDocument> findByDocumento(String documento);
}
