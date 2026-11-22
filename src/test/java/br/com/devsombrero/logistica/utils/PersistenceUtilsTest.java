package br.com.devsombrero.logistica.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    void genericoDeveManterAtualQuandoNovoNulo() {
        LocalDate atual = LocalDate.of(2026, 1, 1);
        LocalDate novo = LocalDate.of(2027, 1, 1);

        assertThat(PersistenceUtils.valorOuAtual(atual, null)).isEqualTo(atual);
        assertThat(PersistenceUtils.valorOuAtual(atual, novo)).isEqualTo(novo);
    }

    @Test
    void deveSerInstanciavel() {
        assertThat(new PersistenceUtils()).isNotNull();
    }
}
