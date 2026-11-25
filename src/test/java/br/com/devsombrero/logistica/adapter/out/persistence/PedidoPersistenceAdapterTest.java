package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.pedido.PedidoDocument;
import br.com.devsombrero.logistica.adapter.out.persistence.pedido.PedidoMongoRepository;
import br.com.devsombrero.logistica.adapter.out.persistence.pedido.PedidoPersistenceAdapter;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Pedido;
import br.com.devsombrero.logistica.support.PedidoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
class PedidoPersistenceAdapterTest {

    private static final String ID = PedidoFixture.ID;
    private static final Sort ORDENACAO = Sort.by(Sort.Direction.DESC, "dataPedido");

    @Mock
    private PedidoMongoRepository repository;

    @InjectMocks
    private PedidoPersistenceAdapter adapter;

    private final Pedido pedido = PedidoFixture.pedidoSalvo();
    private final PedidoDocument document = PedidoDocument.fromDomain(pedido);

    @Test
    void buscarPaginadoDeveConsultarComOrdenacaoEConverter() {
        Pageable pageable = PageRequest.of(1, 2, ORDENACAO);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(document), pageable, 3));

        Paginado<Pedido> resultado = adapter.buscarPaginado(2, 1);

        assertThat(resultado.conteudo()).containsExactly(pedido);
        assertThat(resultado.paginaAtual()).isEqualTo(1);
        assertThat(resultado.tamanhoPagina()).isEqualTo(2);
        assertThat(resultado.totalPaginas()).isEqualTo(2);
        assertThat(resultado.totalElementos()).isEqualTo(3L);
    }

    @Test
    void buscarPaginadoDeveUsarPadroesQuandoParametrosInvalidos() {
        Pageable pageable = PageRequest.of(0, 20, ORDENACAO);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 0));

        Paginado<Pedido> resultado = adapter.buscarPaginado(0, -1);

        assertThat(resultado.conteudo()).isEmpty();
        assertThat(resultado.paginaAtual()).isZero();
        assertThat(resultado.tamanhoPagina()).isEqualTo(20);
    }

    @Test
    void buscarPedidosPorClienteDeveConsultarPorCliente() {
        Pageable pageable = PageRequest.of(0, 10, ORDENACAO);
        when(repository.findByClienteId(PedidoFixture.CLIENTE_ID, pageable))
                .thenReturn(new PageImpl<>(List.of(document), pageable, 1));

        Paginado<Pedido> resultado = adapter.buscarPedidosPorCliente(PedidoFixture.CLIENTE_ID, 10, 0);

        assertThat(resultado.conteudo()).containsExactly(pedido);
        assertThat(resultado.totalElementos()).isEqualTo(1L);
    }

    @Test
    void buscarPedidoPorStatusDeveConsultarPorStatus() {
        Pageable pageable = PageRequest.of(0, 10, ORDENACAO);
        when(repository.findByStatusPedido(StatusPedido.PENDENTE, pageable))
                .thenReturn(new PageImpl<>(List.of(document), pageable, 1));

        Paginado<Pedido> resultado = adapter.buscarPedidoPorStatus(StatusPedido.PENDENTE, 10, 0);

        assertThat(resultado.conteudo()).containsExactly(pedido);
        assertThat(resultado.totalPaginas()).isEqualTo(1);
    }

    @Test
    void buscarPorIdDeveConverterDocumento() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));

        assertThat(adapter.buscarPorId(ID)).contains(pedido);
    }

    @Test
    void buscarPorIdDeveRetornarVazio() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThat(adapter.buscarPorId(ID)).isEmpty();
    }

    @Test
    void salvarDeveConverterEPersistir() {
        when(repository.save(document)).thenReturn(document);

        assertThat(adapter.salvar(pedido)).isEqualTo(pedido);
    }

    @Test
    void atualizarDeveMesclarEPersistir() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));
        when(repository.save(any(PedidoDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido atualizado = adapter.atualizar(ID, PedidoFixture.pedido(StatusPedido.CANCELADO));

        assertThat(atualizado).isEqualTo(PedidoFixture.pedido(StatusPedido.CANCELADO));
    }

    @Test
    void atualizarDeveLancarQuandoNaoEncontrado() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.atualizar(ID, pedido))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Pedido " + ID + " não encontrado");
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
                .hasMessage("Pedido " + ID + " não encontrado");
        verify(repository, never()).deleteById(anyString());
    }
}
