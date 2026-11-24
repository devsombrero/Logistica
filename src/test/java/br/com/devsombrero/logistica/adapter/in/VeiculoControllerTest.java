package br.com.devsombrero.logistica.adapter.in;

import br.com.devsombrero.logistica.adapter.in.rest.veiculo.VeiculoController;
import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.enumerator.StatusVeiculo;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import br.com.devsombrero.logistica.domain.service.VeiculoService;
import br.com.devsombrero.logistica.support.VeiculoFixture;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VeiculoController.class)
class VeiculoControllerTest {

    private static final String URL = "/v1/veiculos";
    private static final String ID = VeiculoFixture.ID;
    private static final String PLACA = VeiculoFixture.PLACA;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VeiculoService veiculoService;

    private final RetornoVeiculo retorno = VeiculoFixture.retornoVeiculo();

    @Test
    void buscarPaginadoDeveUsarValoresPadrao() throws Exception {
        when(veiculoService.buscarVeiculoPaginado(eq(0), eq(10), any()))
                .thenReturn(new Paginado<>(List.of(retorno), 0, 10, 1, 1L));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(1)))
                .andExpect(jsonPath("$.conteudo[0].placa").value(PLACA))
                .andExpect(jsonPath("$.paginaAtual").value(0))
                .andExpect(jsonPath("$.tamanhoPagina").value(10))
                .andExpect(jsonPath("$.totalPaginas").value(1))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    void buscarPaginadoDeveRepassarParametrosEFiltros() throws Exception {
        when(veiculoService.buscarVeiculoPaginado(anyInt(), anyInt(), any()))
                .thenReturn(new Paginado<>(List.of(), 2, 5, 0, 0L));

        mockMvc.perform(get(URL)
                        .param("paginaAtual", "2")
                        .param("tamanhoPagina", "5")
                        .param("modelo", "FH")
                        .param("marca", "Volvo")
                        .param("capacidadeCarga", "25000")
                        .param("status", "EM_USO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(0)));

        ArgumentCaptor<FiltroPesquisaVeiculo> captor = ArgumentCaptor.forClass(FiltroPesquisaVeiculo.class);
        verify(veiculoService).buscarVeiculoPaginado(eq(2), eq(5), captor.capture());
        assertThat(captor.getValue()).isEqualTo(
                new FiltroPesquisaVeiculo("FH", "Volvo", new BigDecimal("25000"), StatusVeiculo.EM_USO)
        );
    }

    @Test
    void cadastrarDeveRetornar201() throws Exception {
        when(veiculoService.cadastrar(any())).thenReturn(retorno);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VeiculoFixture.jsonValido()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.placa").value(PLACA));

        ArgumentCaptor<Veiculo> captor = ArgumentCaptor.forClass(Veiculo.class);
        verify(veiculoService).cadastrar(captor.capture());
        assertThat(captor.getValue()).isEqualTo(VeiculoFixture.novoVeiculo());
    }

    @Test
    void cadastrarComCamposObrigatoriosAusentesDeveRetornar400() throws Exception {
        String body = VeiculoFixture.json("", " ", null, "null", null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Erro de validação dos campos da requisição"))
                .andExpect(jsonPath("$.detalhes", hasSize(5)))
                .andExpect(jsonPath("$.detalhes", hasItems(
                        "placa: Placa é obrigatória",
                        "modelo: Modelo é obrigatório",
                        "marca: Marca é obrigatória",
                        "capacidadeCarga: Capacidade de carga é obrigatória",
                        "status: Status é obrigatório"
                )));

        verifyNoInteractions(veiculoService);
    }

    @Test
    void cadastrarComCapacidadeNaoPositivaDeveRetornar400() throws Exception {
        String body = VeiculoFixture.json(PLACA, "FH", "Volvo", "0", "DISPONIVEL");

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes", hasItems(
                        "capacidadeCarga: Capacidade de carga deve ser maior que zero"
                )));

        verifyNoInteractions(veiculoService);
    }

    @Test
    void cadastrarDuplicadoDeveRetornar422() throws Exception {
        when(veiculoService.cadastrar(any()))
                .thenThrow(new EntidadeUnprocessableException("Já existe um veículo cadastrado com essa placa."));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VeiculoFixture.jsonValido()))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.mensagem").value("Já existe um veículo cadastrado com essa placa."));
    }

    @Test
    void buscarPorIdDeveRetornarVeiculo() throws Exception {
        when(veiculoService.buscarPorId(ID)).thenReturn(retorno);

        mockMvc.perform(get(URL + "/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value(VeiculoFixture.MODELO));
    }

    @Test
    void buscarPorIdInexistenteDeveRetornar404() throws Exception {
        when(veiculoService.buscarPorId("x"))
                .thenThrow(new EntidadeNotFoundException("Veiculo não encontrado com o ID informado"));

        mockMvc.perform(get(URL + "/{id}", "x"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Veiculo não encontrado com o ID informado"))
                .andExpect(jsonPath("$.path").value(URL + "/x"));
    }

    @Test
    void buscarPorPlacaDeveRetornarVeiculo() throws Exception {
        when(veiculoService.buscarPorPlaca(PLACA)).thenReturn(retorno);

        mockMvc.perform(get(URL + "/placa/{placa}", PLACA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));
    }

    @Test
    void atualizarDeveRetornar200() throws Exception {
        when(veiculoService.atualizar(eq(ID), any())).thenReturn(retorno);

        mockMvc.perform(patch(URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"MANUTENCAO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));

        ArgumentCaptor<Veiculo> captor = ArgumentCaptor.forClass(Veiculo.class);
        verify(veiculoService).atualizar(eq(ID), captor.capture());
        assertThat(captor.getValue().status()).isEqualTo(StatusVeiculo.MANUTENCAO);
        assertThat(captor.getValue().placa()).isNull();
    }

    @Test
    void apagarDeveRetornar204() throws Exception {
        mockMvc.perform(delete(URL + "/{id}", ID))
                .andExpect(status().isNoContent());

        verify(veiculoService).apagar(ID);
    }
}
