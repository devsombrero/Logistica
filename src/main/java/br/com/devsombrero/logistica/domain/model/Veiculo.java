package br.com.devsombrero.logistica.domain.model;

import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Veiculo(
        String id,
        String placa,
        String modelo,
        String marca,
        BigDecimal capacidadeCarga,
        StatusVeiculo status,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
    public Veiculo(
            String placa,
            String modelo,
            String marca,
            BigDecimal capacidadeCarga,
            StatusVeiculo status
    ) {
        this(
                null,
                placa,
                modelo,
                marca,
                capacidadeCarga,
                status,
                null,
                null
        );
    }
}
