package br.com.devsombrero.logistica.adapter.out.persistence.cliente;

import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClienteMongoRepository extends MongoRepository<ClienteDocument, String> {
    boolean existsByEmailOrDocumento(String email, String documento);
    Optional<ClienteDocument> findByEmail(String email);
    Optional<ClienteDocument> findByDocumento(String documento);

    @ExistsQuery("""
        {
          '_id': { '$ne': ?2 },
          '$or': [
            { 'email': ?0 },
            { 'documento': ?1 }
          ]
        }
        """)
    boolean existeEmailOuDocumentoEmOutroCliente(
            String email,
            String documento,
            String id
    );
}
