package br.com.devsombrero.logistica.adapter.out.persistence.veiculo;

import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static br.com.devsombrero.logistica.utils.PersistenceUtils.valorOuAtual;

@Document("veiculos")
public record VeiculoDocument(
        @Id
        String id,
        String placa,
        String modelo,
        String marca,
        BigDecimal capacidadeCarga,
        StatusVeiculo status,
        @CreatedDate
        LocalDateTime dataCriacao,
        @LastModifiedDate
        LocalDateTime dataAtualizacao
) {
    public static VeiculoDocument fromDomain(Veiculo veiculo) {
        return new VeiculoDocument(
                veiculo.id(),
                veiculo.placa(),
                veiculo.modelo(),
                veiculo.marca(),
                veiculo.capacidadeCarga(),
                veiculo.status(),
                veiculo.dataCriacao(),
                veiculo.dataAtualizacao()
        );
    }

    public static Veiculo toDomain(VeiculoDocument veiculoDocument) {
        return new Veiculo(
                veiculoDocument.id(),
                veiculoDocument.placa(),
                veiculoDocument.modelo(),
                veiculoDocument.marca(),
                veiculoDocument.capacidadeCarga(),
                veiculoDocument.status(),
                veiculoDocument.dataCriacao(),
                veiculoDocument.dataAtualizacao()
        );
    }

    public VeiculoDocument atualizar(Veiculo veiculo) {
        return new VeiculoDocument(
                this.id,
                valorOuAtual(this.placa, veiculo.placa()),
                valorOuAtual(this.modelo, veiculo.modelo()),
                valorOuAtual(this.marca, veiculo.marca()),
                valorOuAtual(this.capacidadeCarga, veiculo.capacidadeCarga()),
                valorOuAtual(this.status, veiculo.status()),
                this.dataCriacao,
                this.dataAtualizacao
        );
    }

}
