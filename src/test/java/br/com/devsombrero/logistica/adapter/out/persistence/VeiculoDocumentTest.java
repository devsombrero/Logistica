package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.veiculo.VeiculoDocument;
import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import br.com.devsombrero.logistica.support.VeiculoFixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VeiculoDocumentTest {

    @Test
    void fromDomainEToDomainDevemSerSimetricos() {
        Veiculo veiculo = VeiculoFixture.veiculoSalvo();

        VeiculoDocument document = VeiculoDocument.fromDomain(veiculo);

        assertThat(document.id()).isEqualTo(veiculo.id());
        assertThat(document.dataCriacao()).isEqualTo(veiculo.dataCriacao());
        assertThat(VeiculoDocument.toDomain(document)).isEqualTo(veiculo);
    }

    @Test
    void atualizarDeveSubstituirApenasValoresPreenchidos() {
        VeiculoDocument original = VeiculoDocument.fromDomain(VeiculoFixture.veiculoSalvo());
        Veiculo alteracao = new Veiculo("XYZ9K87", " ", null, new BigDecimal("1000"), StatusVeiculo.MANUTENCAO);

        VeiculoDocument atualizado = original.atualizar(alteracao);

        assertThat(atualizado.id()).isEqualTo(original.id());
        assertThat(atualizado.placa()).isEqualTo("XYZ9K87");
        assertThat(atualizado.modelo()).isEqualTo(original.modelo());
        assertThat(atualizado.marca()).isEqualTo(original.marca());
        assertThat(atualizado.capacidadeCarga()).isEqualTo(new BigDecimal("1000"));
        assertThat(atualizado.status()).isEqualTo(StatusVeiculo.MANUTENCAO);
        assertThat(atualizado.dataCriacao()).isEqualTo(original.dataCriacao());
        assertThat(atualizado.dataAtualizacao()).isEqualTo(original.dataAtualizacao());
    }

    @Test
    void atualizarDeveManterValoresQuandoNulos() {
        VeiculoDocument original = VeiculoDocument.fromDomain(VeiculoFixture.veiculoSalvo());
        Veiculo alteracao = new Veiculo(null, null, null, null, null);

        VeiculoDocument atualizado = original.atualizar(alteracao);

        assertThat(atualizado).isEqualTo(original);
    }
}
