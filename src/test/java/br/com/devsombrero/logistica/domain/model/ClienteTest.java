package br.com.devsombrero.logistica.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteTest {

    @Test
    void construtorSemIdDeveDeixarIdEDatasNulos() {
        Cliente cliente = new Cliente("Maria", "123", "1199", "m@e.com", "Rua A", "Campinas", "SP");

        assertThat(cliente.id()).isNull();
        assertThat(cliente.dataCriacao()).isNull();
        assertThat(cliente.dataAtualizacao()).isNull();
        assertThat(cliente.nome()).isEqualTo("Maria");
        assertThat(cliente.documento()).isEqualTo("123");
        assertThat(cliente.telefone()).isEqualTo("1199");
        assertThat(cliente.email()).isEqualTo("m@e.com");
        assertThat(cliente.endereco()).isEqualTo("Rua A");
        assertThat(cliente.cidade()).isEqualTo("Campinas");
        assertThat(cliente.estado()).isEqualTo("SP");
    }

    @Test
    void paginadoDeveExporComponentes() {
        Paginado<String> paginado = new Paginado<>(List.of("a"), 1, 2, 3, 4L);

        assertThat(paginado.conteudo()).containsExactly("a");
        assertThat(paginado.paginaAtual()).isEqualTo(1);
        assertThat(paginado.tamanhoPagina()).isEqualTo(2);
        assertThat(paginado.totalPaginas()).isEqualTo(3);
        assertThat(paginado.totalElementos()).isEqualTo(4L);
    }
}
