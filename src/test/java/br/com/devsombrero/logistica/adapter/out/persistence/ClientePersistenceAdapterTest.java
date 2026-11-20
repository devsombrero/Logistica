package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.cliente.ClienteDocument;
import br.com.devsombrero.logistica.adapter.out.persistence.cliente.ClienteMongoRepository;
import br.com.devsombrero.logistica.adapter.out.persistence.cliente.ClientePersistenceAdapter;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.support.ClienteFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientePersistenceAdapterTest {

    private static final String ID = ClienteFixture.ID;

    @Mock
    private ClienteMongoRepository repository;

    @InjectMocks
    private ClientePersistenceAdapter adapter;

    private final ClienteDocument document = ClienteDocument.fromDomain(ClienteFixture.clienteSalvo());

    @Test
    void buscarPaginadoDeveConsultarComPaginacaoEConverter() {
        Pageable pageable = PageRequest.of(1, 2);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(document), pageable, 3));

        Paginado<Cliente> resultado = adapter.buscarPaginado(2, 1);

        assertThat(resultado.conteudo()).containsExactly(ClienteFixture.clienteSalvo());
        assertThat(resultado.paginaAtual()).isEqualTo(1);
        assertThat(resultado.tamanhoPagina()).isEqualTo(2);
        assertThat(resultado.totalPaginas()).isEqualTo(2);
        assertThat(resultado.totalElementos()).isEqualTo(3L);
        verify(repository).findAll(pageable);
    }

    @Test
    void buscarPorIdDeveConverterDocumento() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));

        assertThat(adapter.buscarPorId(ID)).contains(ClienteFixture.clienteSalvo());
    }

    @Test
    void buscarPorIdDeveRetornarVazio() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThat(adapter.buscarPorId(ID)).isEmpty();
    }

    @Test
    void buscarPorEmailDeveConverterDocumento() {
        when(repository.findByEmail("maria@email.com")).thenReturn(Optional.of(document));

        assertThat(adapter.buscarPorEmail("maria@email.com")).contains(ClienteFixture.clienteSalvo());
    }

    @Test
    void buscarPorDocumentoDeveConverterDocumento() {
        when(repository.findByDocumento("12345678900")).thenReturn(Optional.of(document));

        assertThat(adapter.buscarPorDocumento("12345678900")).contains(ClienteFixture.clienteSalvo());
    }

    @Test
    void existePorEmailOuDocumentoDeveDelegar() {
        when(repository.existsByEmailOrDocumento("e", "d")).thenReturn(true);

        assertThat(adapter.existePorEmailOuDocumento("e", "d")).isTrue();
    }

    @Test
    void existeEmailOuDocumentoEmOutroClienteDeveDelegar() {
        when(repository.existeEmailOuDocumentoEmOutroCliente("e", "d", ID)).thenReturn(true);

        assertThat(adapter.existeEmailOuDocumentoEmOutroCliente("e", "d", ID)).isTrue();
    }

    @Test
    void salvarDeveConverterEPersistir() {
        Cliente novo = ClienteFixture.novoCliente();
        when(repository.save(ClienteDocument.fromDomain(novo))).thenReturn(document);

        assertThat(adapter.salvar(novo)).isEqualTo(ClienteFixture.clienteSalvo());
    }

    @Test
    void atualizarDeveMesclarEPersistir() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));
        when(repository.save(any(ClienteDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente alteracao = new Cliente("Maria Souza", null, null, null, null, null, null);
        Cliente atualizado = adapter.atualizar(ID, alteracao);

        assertThat(atualizado.nome()).isEqualTo("Maria Souza");
        assertThat(atualizado.email()).isEqualTo("maria@email.com");
        ArgumentCaptor<ClienteDocument> captor = ArgumentCaptor.forClass(ClienteDocument.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(ID);
    }

    @Test
    void atualizarDeveLancarQuandoNaoEncontrado() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Cliente alteracao = ClienteFixture.novoCliente();
        assertThatThrownBy(() -> adapter.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Cliente não encontrado: " + ID);
        verify(repository, never()).save(any());
    }

    @Test
    void deletarDeveRemoverQuandoExiste() {
        when(repository.existsById(ID)).thenReturn(true);

        adapter.deletar(ID);

        verify(repository).deleteById(ID);
    }

    @Test
    void deletarDeveLancarQuandoNaoExiste() {
        when(repository.existsById(ID)).thenReturn(false);

        assertThatThrownBy(() -> adapter.deletar(ID))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Cliente não encontrado: " + ID);
        verify(repository, never()).deleteById(anyString());
    }
}
