package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.veiculo.VeiculoDocument;
import br.com.devsombrero.logistica.adapter.out.persistence.veiculo.VeiculoMongoRepository;
import br.com.devsombrero.logistica.adapter.out.persistence.veiculo.VeiculoPersistenceAdapter;
import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import br.com.devsombrero.logistica.support.VeiculoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VeiculoPersistenceAdapterTest {

    private static final String ID = VeiculoFixture.ID;
    private static final String PLACA = VeiculoFixture.PLACA;

    @Mock
    private VeiculoMongoRepository repository;
    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private VeiculoPersistenceAdapter adapter;

    private final VeiculoDocument document = VeiculoDocument.fromDomain(VeiculoFixture.veiculoSalvo());

    @Test
    void existePorPlacaDeveDelegar() {
        when(repository.existsByPlaca(PLACA)).thenReturn(true);

        assertThat(adapter.existePorPlaca(PLACA)).isTrue();
    }

    @Test
    void buscarPorPlacaDeveDelegar() {
        when(repository.findByPlaca(PLACA)).thenReturn(Optional.of(VeiculoFixture.veiculoSalvo()));

        assertThat(adapter.buscarPorPlaca(PLACA)).contains(VeiculoFixture.veiculoSalvo());
    }

    @Test
    void existePorPlacaIdDiferenteDeveDelegar() {
        when(repository.existePlacaIdDiferente(PLACA, ID)).thenReturn(true);

        assertThat(adapter.existePorPlacaIdDiferente(PLACA, ID)).isTrue();
    }

    @Test
    void buscarPaginadoComFiltrosDeveAplicarTodosOsCriterios() {
        FiltroPesquisaVeiculo filtro = new FiltroPesquisaVeiculo(
                "FH", "volvo", new BigDecimal("25000.00"), StatusVeiculo.DISPONIVEL
        );
        when(mongoTemplate.count(any(Query.class), eq(VeiculoDocument.class))).thenReturn(3L);
        when(mongoTemplate.find(any(Query.class), eq(VeiculoDocument.class))).thenReturn(List.of(document));

        Paginado<Veiculo> resultado = adapter.buscarPaginadoComFiltros(1, 2, filtro);

        assertThat(resultado.conteudo()).containsExactly(VeiculoFixture.veiculoSalvo());
        assertThat(resultado.paginaAtual()).isEqualTo(1);
        assertThat(resultado.tamanhoPagina()).isEqualTo(2);
        assertThat(resultado.totalPaginas()).isEqualTo(2);
        assertThat(resultado.totalElementos()).isEqualTo(3L);

        ArgumentCaptor<Query> captor = ArgumentCaptor.forClass(Query.class);
        verify(mongoTemplate).find(captor.capture(), eq(VeiculoDocument.class));
        Query query = captor.getValue();
        String criterios = query.getQueryObject().toString();
        assertThat(criterios)
                .contains("modelo", "FH", "marca", "volvo", "capacidadeCarga", "status", "DISPONIVEL");
        assertThat(query.getSkip()).isEqualTo(2L);
        assertThat(query.getLimit()).isEqualTo(2);
    }

    @Test
    void buscarPaginadoComFiltrosVaziosNaoDeveAdicionarCriterios() {
        FiltroPesquisaVeiculo filtro = new FiltroPesquisaVeiculo(null, null, null, null);
        when(mongoTemplate.count(any(Query.class), eq(VeiculoDocument.class))).thenReturn(0L);
        when(mongoTemplate.find(any(Query.class), eq(VeiculoDocument.class))).thenReturn(List.of());

        Paginado<Veiculo> resultado = adapter.buscarPaginadoComFiltros(0, 10, filtro);

        assertThat(resultado.conteudo()).isEmpty();
        assertThat(resultado.totalPaginas()).isZero();
        assertThat(resultado.totalElementos()).isZero();

        ArgumentCaptor<Query> captor = ArgumentCaptor.forClass(Query.class);
        verify(mongoTemplate).find(captor.capture(), eq(VeiculoDocument.class));
        assertThat(captor.getValue().getQueryObject()).isEmpty();
    }

    @Test
    void buscarPaginadoComFiltrosEmBrancoNaoDeveAdicionarCriterios() {
        FiltroPesquisaVeiculo filtro = new FiltroPesquisaVeiculo(" ", "", null, null);
        when(mongoTemplate.count(any(Query.class), eq(VeiculoDocument.class))).thenReturn(0L);
        when(mongoTemplate.find(any(Query.class), eq(VeiculoDocument.class))).thenReturn(List.of());

        adapter.buscarPaginadoComFiltros(0, 10, filtro);

        ArgumentCaptor<Query> captor = ArgumentCaptor.forClass(Query.class);
        verify(mongoTemplate).find(captor.capture(), eq(VeiculoDocument.class));
        assertThat(captor.getValue().getQueryObject()).isEmpty();
    }

    @Test
    void buscarPaginadoDeveConsultarComPaginacaoEConverter() {
        Pageable pageable = PageRequest.of(1, 2);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(document), pageable, 3));

        Paginado<Veiculo> resultado = adapter.buscarPaginado(2, 1);

        assertThat(resultado.conteudo()).containsExactly(VeiculoFixture.veiculoSalvo());
        assertThat(resultado.paginaAtual()).isEqualTo(1);
        assertThat(resultado.tamanhoPagina()).isEqualTo(2);
        assertThat(resultado.totalPaginas()).isEqualTo(2);
        assertThat(resultado.totalElementos()).isEqualTo(3L);
    }

    @Test
    void buscarPorIdDeveConverterDocumento() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));

        assertThat(adapter.buscarPorId(ID)).contains(VeiculoFixture.veiculoSalvo());
    }

    @Test
    void buscarPorIdDeveRetornarVazio() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThat(adapter.buscarPorId(ID)).isEmpty();
    }

    @Test
    void salvarDeveConverterEPersistir() {
        Veiculo novo = VeiculoFixture.novoVeiculo();
        when(repository.save(VeiculoDocument.fromDomain(novo))).thenReturn(document);

        assertThat(adapter.salvar(novo)).isEqualTo(VeiculoFixture.veiculoSalvo());
    }

    @Test
    void atualizarDeveMesclarEPersistir() {
        when(repository.findById(ID)).thenReturn(Optional.of(document));
        when(repository.save(any(VeiculoDocument.class))).thenAnswer(inv -> inv.getArgument(0));

        Veiculo alteracao = new Veiculo(null, "FH 460", null, null, StatusVeiculo.INATIVO);
        Veiculo atualizado = adapter.atualizar(ID, alteracao);

        assertThat(atualizado.modelo()).isEqualTo("FH 460");
        assertThat(atualizado.status()).isEqualTo(StatusVeiculo.INATIVO);
        assertThat(atualizado.placa()).isEqualTo(PLACA);
        ArgumentCaptor<VeiculoDocument> captor = ArgumentCaptor.forClass(VeiculoDocument.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(ID);
    }

    @Test
    void atualizarDeveLancarQuandoNaoEncontrado() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Veiculo alteracao = VeiculoFixture.novoVeiculo();
        assertThatThrownBy(() -> adapter.atualizar(ID, alteracao))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Veículo não encontrado");
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
                .hasMessage("Veículo não encontrado");
        verify(repository, never()).deleteById(anyString());
    }
}
