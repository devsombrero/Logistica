package br.com.devsombrero.logistica.adapter.in.dto;

import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RequisicaoVeiculo(

        @NotBlank(message = "Placa é obrigatória")
        String placa,

        @NotBlank(message = "Modelo é obrigatório")
        String modelo,

        @NotBlank(message = "Marca é obrigatória")
        String marca,

        @NotNull(message = "Capacidade de carga é obrigatória")
        @Positive(message = "Capacidade de carga deve ser maior que zero")
        BigDecimal capacidadeCarga,

        @NotNull(message = "Status é obrigatório")
        StatusVeiculo status

) {
    public Veiculo toDomain() {
        return new Veiculo(
                placa,
                modelo,
                marca,
                capacidadeCarga,
                status
        );
    }
}