package br.com.devsombrero.logistica.domain.model;

import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VeiculoTest {

    @Test
    void construtorSemIdDeveDeixarIdEDatasNulos() {
        Veiculo veiculo = new Veiculo("PLA1234", "Modelo", "Marca", BigDecimal.TEN, StatusVeiculo.EM_USO);

        assertThat(veiculo.id()).isNull();
        assertThat(veiculo.dataCriacao()).isNull();
        assertThat(veiculo.dataAtualizacao()).isNull();
        assertThat(veiculo.placa()).isEqualTo("PLA1234");
        assertThat(veiculo.modelo()).isEqualTo("Modelo");
        assertThat(veiculo.marca()).isEqualTo("Marca");
        assertThat(veiculo.capacidadeCarga()).isEqualTo(BigDecimal.TEN);
        assertThat(veiculo.status()).isEqualTo(StatusVeiculo.EM_USO);
    }

    @Test
    void statusVeiculoDeveConterTodosOsValores() {
        assertThat(StatusVeiculo.values()).containsExactly(
                StatusVeiculo.DISPONIVEL,
                StatusVeiculo.EM_USO,
                StatusVeiculo.MANUTENCAO,
                StatusVeiculo.INATIVO
        );
        assertThat(StatusVeiculo.valueOf("MANUTENCAO")).isEqualTo(StatusVeiculo.MANUTENCAO);
    }
}
