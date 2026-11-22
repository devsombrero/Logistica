package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.motorista.MotoristaDocument;
import br.com.devsombrero.logistica.adapter.out.persistence.motorista.MotoristaMongoRepository;
import br.com.devsombrero.logistica.adapter.out.persistence.motorista.MotoristaPersistenceAdapter;
import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.support.MotoristaFixture;
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
class MotoristaPersistenceAdapterTest {

    private static final String ID = MotoristaFixture.ID;
    private static final String CPF = MotoristaFixture.CPF;
    private static final String CNH = MotoristaFixture.CNH;

    @Mock
    private MotoristaMongoRepository repository;

    @InjectMocks
    private MotoristaPersistenceAdapter adapter;

    private final MotoristaDocument document = MotoristaDocument.fromDomain(MotoristaFixture.motoristaSalvo());

    @Test
    void existePorCpfDeveDelegar() {
        when(repository.existsByCpf(CPF)).thenReturn(true);

        assertThat(adapter.existePorCpf(CPF)).isTrue();
    }

    @Test
    void existePorCnhDeveDelegar() {
        when(repository.existsByCnh(CNH)).thenReturn(true);

        assertThat(adapter.existePorCnh(CNH)).isTrue();
    }

    @Test
    void buscarPorCnhDeveDelegar() {
        when(repository.findByCnh(CNH)).thenReturn(Optional.of(MotoristaFixture.motoristaSalvo()));

        assertThat(adapter.buscarPorCnh(CNH)).contains(MotoristaFixture.motoristaSalvo());
    }

    @Test
    void buscarPorCpfDeveDelegar() {
        when(repository.findByCpf(CPF)).thenReturn(Optional.of(MotoristaFixture.motoristaSalvo()));

        assertThat(adapter.buscarPorCpf(CPF)).contains(MotoristaFixture.motoristaSalvo());
    }

    @Test
    void existeCnhOuCpfEmOutroMotoristaDeveDelegar() {
        when(repository.existeCnhOuCpfEmOutroMotorista(CNH, CPF, ID)).thenReturn(true);

        assertThat(adapter.existeCnhOuCpfEmOutroMotorista(CNH, CPF, ID)).isTrue();
    }

    @Test
    void buscarPaginadoDeveConsultarComPaginacaoEConverter() {
        Pageable pageable = PageRequest.of(1, 2);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(document), pageable, 3));

        Paginado<Motorista> resultado = adapter.buscarPaginado(2, 1);

        assertThat(resultado.conteudo()).containsExactly(MotoristaFixture.motoristaSalvo());
        assertThat(resultado.paginaAtual()).isEqualTo(1);
        assertThat(resultado.tamanhoPagina()).isEqualTo(2);
        assertThat(resultado.totalPaginas()).isEqualTo(2);
        assertThat(resultado.totalElementos()).isEqualTo(3L);
    }

    @Test
    void buscarPorIdDeveConverterDocumento() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));

        assertThat(adapter.buscarPorId(ID)).contains(MotoristaFixture.motoristaSalvo());
    }

    @Test
    void buscarPorIdDeveRetornarVazio() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThat(adapter.buscarPorId(ID)).isEmpty();
    }

    @Test
    void salvarDeveConverterEPersistir() {
        Motorista novo = MotoristaFixture.novoMotorista();
        when(repository.save(MotoristaDocument.fromDomain(novo))).thenReturn(document);

        assertThat(adapter.salvar(novo)).isEqualTo(MotoristaFixture.motoristaSalvo());
    }

    @Test
    void atualizarDeveMesclarEPersistir() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));
        when(repository.save(any(MotoristaDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        Motorista alteracao = new Motorista("Maria", null, null, null, null, StatusMotorista.INATIVO);
        Motorista atualizado = adapter.atualizar(ID, alteracao);

        assertThat(atualizado.nome()).isEqualTo("Maria");
        assertThat(atualizado.status()).isEqualTo(StatusMotorista.INATIVO);
        assertThat(atualizado.cpf()).isEqualTo(CPF);
        ArgumentCaptor<MotoristaDocument> captor = ArgumentCaptor.forClass(MotoristaDocument.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(ID);
    }

    @Test
    void atualizarDeveLancarQuandoNaoEncontrado() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Motorista alteracao = MotoristaFixture.novoMotorista();
        assertThatThrownBy(() -> adapter.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Motorista não encontrado: " + ID);
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
                .hasMessage("Motorista não encontrado: " + ID);
        verify(repository, never()).deleteById(anyString());
    }
}
