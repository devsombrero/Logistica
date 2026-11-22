package br.com.devsombrero.logistica.domain.mapper;

import br.com.devsombrero.logistica.support.MotoristaFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MotoristaMapperTest {

    @Test
    void deveMapearMotoristaParaRetorno() {
        assertThat(MotoristaMapper.toResponse(MotoristaFixture.motoristaSalvo()))
                .isEqualTo(MotoristaFixture.retornoMotorista());
    }

    @Test
    void deveSerInstanciavel() {
        assertThat(new MotoristaMapper()).isNotNull();
    }
}
