package br.com.devsombrero.logistica.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PaginadoTest {

    @Test
    void mapDeveConverterConteudoEPreservarMetadados() {
        Paginado<Integer> origem = new Paginado<>(List.of(1, 2), 3, 10, 4, 35L);

        Paginado<String> resultado = origem.map(String::valueOf);

        assertThat(resultado.conteudo()).containsExactly("1", "2");
        assertThat(resultado.paginaAtual()).isEqualTo(3);
        assertThat(resultado.tamanhoPagina()).isEqualTo(10);
        assertThat(resultado.totalPaginas()).isEqualTo(4);
        assertThat(resultado.totalElementos()).isEqualTo(35L);
    }
}
