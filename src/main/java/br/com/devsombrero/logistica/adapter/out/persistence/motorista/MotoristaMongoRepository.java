package br.com.devsombrero.logistica.adapter.out.persistence.motorista;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MotoristaMongoRepository extends MongoRepository<MotoristaDocument, String> {
    boolean existsByCpf(String cpf);
    boolean existsByCnh(String cnh);
}
