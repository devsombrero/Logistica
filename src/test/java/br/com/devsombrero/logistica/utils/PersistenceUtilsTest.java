package br.com.devsombrero.logistica.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PersistenceUtilsTest {

    @Test
    void stringDeveManterAtualQuandoNovoNuloOuEmBranco() {
        assertThat(PersistenceUtils.valorOuAtual("atual", null)).isEqualTo("atual");
        assertThat(PersistenceUtils.valorOuAtual("atual", "")).isEqualTo("atual");
        assertThat(PersistenceUtils.valorOuAtual("atual", "  ")).isEqualTo("atual");
        assertThat(PersistenceUtils.valorOuAtual("atual", "novo")).isEqualTo("novo");
    }

    @Test
    void bigDecimalDeveManterAtualQuandoNovoNulo() {
        assertThat(PersistenceUtils.valorOuAtual(BigDecimal.ONE, null)).isEqualTo(BigDecimal.ONE);
        assertThat(PersistenceUtils.valorOuAtual(BigDecimal.ONE, BigDecimal.TEN)).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void deveSerInstanciavel() {
        assertThat(new PersistenceUtils()).isNotNull();
    }
}
