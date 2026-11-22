package br.com.devsombrero.logistica.adapter.out.persistence.motorista;

import br.com.devsombrero.logistica.domain.model.Motorista;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MotoristaMongoRepository extends MongoRepository<MotoristaDocument, String> {
    boolean existsByCpf(String cpf);
    boolean existsByCnh(String cnh);
    Optional<Motorista> findByCnh(String cnh);
    Optional<Motorista> findByCpf(String cpf);
}
