package br.com.devsombrero.logistica.domain.mapper;

import br.com.devsombrero.logistica.support.VeiculoFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VeiculoMapperTest {

    @Test
    void deveMapearVeiculoParaRetorno() {
        assertThat(VeiculoMapper.toResponse(VeiculoFixture.veiculoSalvo()))
                .isEqualTo(VeiculoFixture.retornoVeiculo());
    }

    @Test
    void deveSerInstanciavel() {
        assertThat(new VeiculoMapper()).isNotNull();
    }
}
