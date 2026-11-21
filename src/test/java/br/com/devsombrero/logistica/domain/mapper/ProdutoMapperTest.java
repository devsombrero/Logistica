package br.com.devsombrero.logistica.domain.mapper;

import br.com.devsombrero.logistica.support.ProdutoFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProdutoMapperTest {

    @Test
    void deveMapearProdutoParaRetorno() {
        assertThat(ProdutoMapper.toResponse(ProdutoFixture.produtoSalvo()))
                .isEqualTo(ProdutoFixture.retornoProduto());
    }

    @Test
    void deveSerInstanciavel() {
        assertThat(new ProdutoMapper()).isNotNull();
    }
}
