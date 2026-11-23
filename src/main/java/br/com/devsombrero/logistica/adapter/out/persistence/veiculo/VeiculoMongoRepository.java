package br.com.devsombrero.logistica.adapter.out.persistence.veiculo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VeiculoMongoRepository extends MongoRepository<VeiculoDocument, String> {
    boolean existsByPlaca(String placa);
}
