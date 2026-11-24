package br.com.devsombrero.logistica.adapter.out.persistence.veiculo;

import br.com.devsombrero.logistica.domain.model.Veiculo;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VeiculoMongoRepository extends MongoRepository<VeiculoDocument, String> {
    boolean existsByPlaca(String placa);
    Optional<Veiculo> findByPlaca(String placa);

    @ExistsQuery("{ 'placa': ?0, '_id': { '$ne': ?1 } }")
    boolean existePlacaIdDiferente(String placa, String id);
}
