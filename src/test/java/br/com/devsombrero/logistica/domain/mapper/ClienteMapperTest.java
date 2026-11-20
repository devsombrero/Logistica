package br.com.devsombrero.logistica.domain.mapper;

import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.support.ClienteFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteMapperTest {

    @Test
    void deveMapearClienteParaRetorno() {
        RetornoCliente retorno = ClienteMapper.toResponse(ClienteFixture.clienteSalvo());

        assertThat(retorno).isEqualTo(ClienteFixture.retornoCliente());
    }

    @Test
    void deveSerInstanciavel() {
        assertThat(new ClienteMapper()).isNotNull();
    }
}
