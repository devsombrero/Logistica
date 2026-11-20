package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.support.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({TestcontainersConfiguration.class, ClientePersistenceAdapter.class})
class ClienteMongoRepositoryIntegrationTest {

    @Autowired
    private ClienteMongoRepository repository;

    @Autowired
    private ClientePersistenceAdapter adapter;

    private Cliente maria;
    private Cliente joao;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        maria = adapter.salvar(new Cliente("Maria", "111", "1199", "maria@email.com", "Rua A", "SP", "SP"));
        joao = adapter.salvar(new Cliente("João", "222", "1188", "joao@email.com", "Rua B", "RJ", "RJ"));
    }

    @Test
    void salvarDeveGerarId() {
        assertThat(maria.id()).isNotBlank();
        assertThat(joao.id()).isNotBlank().isNotEqualTo(maria.id());
    }

    @Test
    void buscasDevemEncontrarPorEmailEDocumento() {
        assertThat(adapter.buscarPorId(maria.id())).contains(maria);
        assertThat(adapter.buscarPorEmail("joao@email.com")).contains(joao);
        assertThat(adapter.buscarPorDocumento("111")).contains(maria);
        assertThat(adapter.buscarPorEmail("ninguem@email.com")).isEmpty();
    }

    @Test
    void existePorEmailOuDocumentoDeveConsiderarQualquerCampo() {
        assertThat(adapter.existePorEmailOuDocumento("maria@email.com", "999")).isTrue();
        assertThat(adapter.existePorEmailOuDocumento("x@email.com", "222")).isTrue();
        assertThat(adapter.existePorEmailOuDocumento("x@email.com", "999")).isFalse();
    }

    @Test
    void existeEmailOuDocumentoEmOutroClienteDeveIgnorarOProprioCliente() {
        assertThat(adapter.existeEmailOuDocumentoEmOutroCliente("maria@email.com", "111", maria.id())).isFalse();
        assertThat(adapter.existeEmailOuDocumentoEmOutroCliente("joao@email.com", "111", maria.id())).isTrue();
        assertThat(adapter.existeEmailOuDocumentoEmOutroCliente("maria@email.com", "222", maria.id())).isTrue();
        assertThat(adapter.existeEmailOuDocumentoEmOutroCliente("novo@email.com", "999", maria.id())).isFalse();
    }

    @Test
    void buscarPaginadoDeveRespeitarTamanhoDaPagina() {
        Paginado<Cliente> primeira = adapter.buscarPaginado(1, 0);
        Paginado<Cliente> segunda = adapter.buscarPaginado(1, 1);

        assertThat(primeira.conteudo()).hasSize(1);
        assertThat(segunda.conteudo()).hasSize(1);
        assertThat(segunda.paginaAtual()).isEqualTo(1);
        assertThat(segunda.tamanhoPagina()).isEqualTo(1);
        assertThat(segunda.totalPaginas()).isEqualTo(2);
        assertThat(segunda.totalElementos()).isEqualTo(2L);
        assertThat(primeira.conteudo().getFirst().id()).isNotEqualTo(segunda.conteudo().getFirst().id());
    }

    @Test
    void atualizarDevePersistirApenasCamposInformados() {
        Cliente atualizado = adapter.atualizar(maria.id(),
                new Cliente("Maria Souza", null, null, null, null, null, null));

        assertThat(atualizado.nome()).isEqualTo("Maria Souza");
        assertThat(adapter.buscarPorId(maria.id()))
                .get()
                .satisfies(c -> {
                    assertThat(c.nome()).isEqualTo("Maria Souza");
                    assertThat(c.email()).isEqualTo("maria@email.com");
                });
    }

    @Test
    void deletarDeveRemoverDocumento() {
        adapter.deletar(maria.id());

        assertThat(repository.existsById(maria.id())).isFalse();
        assertThat(repository.count()).isEqualTo(1);
    }
}
