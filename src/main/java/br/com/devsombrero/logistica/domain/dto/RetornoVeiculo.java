package br.com.devsombrero.logistica.domain.dto;

import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;

import java.math.BigDecimal;

public record RetornoVeiculo(
        String id,
        String placa,
        String modelo,
        String marca,
        BigDecimal capacidadeCarga,
        StatusVeiculo status
) {
}
