package br.com.devsombrero.logistica.adapter.out.persistence.motorista;

import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static br.com.devsombrero.logistica.utils.PersistenceUtils.valorOuAtual;

@Document("motoristas")
public record MotoristaDocument(
        @Id
        String id,
        String nome,
        String cpf,
        String telefone,
        String cnh,
        LocalDate validadeCnh,
        StatusMotorista status,
        @CreatedDate
        LocalDateTime dataCriacao,
        @LastModifiedDate
        LocalDateTime dataAtualizacao
) {

    public static MotoristaDocument fromDomain(Motorista motorista) {
        return new MotoristaDocument(
                motorista.id(),
                motorista.nome(),
                motorista.cpf(),
                motorista.telefone(),
                motorista.cnh(),
                motorista.validadeCnh(),
                motorista.status(),
                motorista.dataCriacao(),
                motorista.dataAtualizacao()
        );
    }

    public static Motorista toDomain(MotoristaDocument motoristaDocument) {
        return new Motorista(
                motoristaDocument.id(),
                motoristaDocument.nome(),
                motoristaDocument.cpf(),
                motoristaDocument.telefone(),
                motoristaDocument.cnh(),
                motoristaDocument.validadeCnh(),
                motoristaDocument.status(),
                motoristaDocument.dataCriacao(),
                motoristaDocument.dataAtualizacao()
        );
    }

    public MotoristaDocument atualizar(Motorista motorista) {
        return new MotoristaDocument(
                this.id,
                valorOuAtual(this.nome, motorista.nome()),
                valorOuAtual(this.cpf, motorista.cpf()),
                valorOuAtual(this.telefone, motorista.telefone()),
                valorOuAtual(this.cnh, motorista.cnh()),
                valorOuAtual(this.validadeCnh, motorista.validadeCnh()),
                valorOuAtual(this.status, motorista.status()),
                this.dataCriacao,
                this.dataAtualizacao
        );
    }
}
