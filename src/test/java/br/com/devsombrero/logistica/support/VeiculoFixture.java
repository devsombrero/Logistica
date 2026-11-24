package br.com.devsombrero.logistica.support;

import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;
import br.com.devsombrero.logistica.domain.model.Veiculo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class VeiculoFixture {

    public static final String ID = "995f1c2e9b1e8a3d4c5b6a9e";
    public static final String PLACA = "ABC1D23";
    public static final String MODELO = "FH 540";
    public static final String MARCA = "Volvo";
    public static final BigDecimal CAPACIDADE = new BigDecimal("25000.00");
    public static final LocalDateTime DATA = LocalDateTime.of(2026, 3, 10, 8, 30);

    private VeiculoFixture() {
    }

    public static Veiculo novoVeiculo() {
        return new Veiculo(PLACA, MODELO, MARCA, CAPACIDADE, StatusVeiculo.DISPONIVEL);
    }

    public static Veiculo veiculoSalvo() {
        return new Veiculo(
                ID,
                PLACA,
                MODELO,
                MARCA,
                CAPACIDADE,
                StatusVeiculo.DISPONIVEL,
                DATA,
                DATA
        );
    }

    public static RetornoVeiculo retornoVeiculo() {
        return new RetornoVeiculo(ID, PLACA, MODELO, MARCA, CAPACIDADE, StatusVeiculo.DISPONIVEL);
    }

    public static String json(String placa, String modelo, String marca, String capacidadeCarga, String status) {
        return """
                {
                  "placa": %s,
                  "modelo": %s,
                  "marca": %s,
                  "capacidadeCarga": %s,
                  "status": %s
                }
                """.formatted(q(placa), q(modelo), q(marca), capacidadeCarga, q(status));
    }

    public static String jsonValido() {
        return json(PLACA, MODELO, MARCA, CAPACIDADE.toPlainString(), "DISPONIVEL");
    }

    private static String q(String valor) {
        return valor == null ? "null" : "\"" + valor + "\"";
    }
}
