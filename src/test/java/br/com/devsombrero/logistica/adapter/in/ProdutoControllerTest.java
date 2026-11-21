package br.com.devsombrero.logistica.adapter.in;

import br.com.devsombrero.logistica.adapter.in.rest.ProdutoController;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Produto;
import br.com.devsombrero.logistica.domain.service.ProdutoService;
import br.com.devsombrero.logistica.support.ProdutoFixture;
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

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    private static final String URL = "/v1/produtos";
    private static final String ID = ProdutoFixture.ID;
    private static final String SKU = ProdutoFixture.SKU;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    private final RetornoProduto retorno = ProdutoFixture.retornoProduto();

    @Test
    void buscarPaginadoDeveUsarValoresPadrao() throws Exception {
        when(produtoService.buscarPaginado(10, 0))
                .thenReturn(new Paginado<>(List.of(retorno), 0, 10, 1, 1L));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(1)))
                .andExpect(jsonPath("$.conteudo[0].sku").value(SKU))
                .andExpect(jsonPath("$.paginaAtual").value(0))
                .andExpect(jsonPath("$.tamanhoPagina").value(10))
                .andExpect(jsonPath("$.totalPaginas").value(1))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    void buscarPaginadoDeveRepassarParametros() throws Exception {
        when(produtoService.buscarPaginado(5, 2))
                .thenReturn(new Paginado<>(List.of(), 2, 5, 0, 0L));

        mockMvc.perform(get(URL).param("tamanhoPagina", "5").param("paginaAtual", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(0)));
    }

    @Test
    void cadastrarDeveRetornar201() throws Exception {
        when(produtoService.cadastrar(any())).thenReturn(retorno);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ProdutoFixture.jsonValido()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.sku").value(SKU));

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoService).cadastrar(captor.capture());
        assertThat(captor.getValue()).isEqualTo(ProdutoFixture.novoProduto());
    }

    @Test
    void cadastrarComCamposInvalidosDeveRetornar400ComDetalhes() throws Exception {
        String body = ProdutoFixture.json("", " ", "", null, "0", "-1", null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Erro de validação dos campos da requisição"))
                .andExpect(jsonPath("$.detalhes", hasSize(7)))
                .andExpect(jsonPath("$.detalhes", hasItems(
                        "sku: SKU é obrigatório",
                        "nome: Nome é obrigatório",
                        "descricao: Descrição é obrigatória",
                        "peso: Peso é obrigatório",
                        "altura: Altura deve ser maior que zero",
                        "comprimento: Comprimento deve ser maior que zero",
                        "valor: Valor é obrigatório"
                )));

        verifyNoInteractions(produtoService);
    }

    @Test
    void cadastrarDuplicadoDeveRetornar422() throws Exception {
        when(produtoService.cadastrar(any()))
                .thenThrow(new EntidadeUnprocessableException("O produto com esse sku já existe na base de dados!"));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ProdutoFixture.jsonValido()))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.mensagem").value("O produto com esse sku já existe na base de dados!"));
    }

    @Test
    void buscarPorIdDeveRetornarProduto() throws Exception {
        when(produtoService.buscarPorId(ID)).thenReturn(retorno);

        mockMvc.perform(get(URL + "/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Caixa"));
    }

    @Test
    void buscarPorIdInexistenteDeveRetornar404() throws Exception {
        when(produtoService.buscarPorId("x"))
                .thenThrow(new EntidadeNotFoundException("Produto não encontrado com esse ID"));

        mockMvc.perform(get(URL + "/{id}", "x"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Produto não encontrado com esse ID"))
                .andExpect(jsonPath("$.path").value(URL + "/x"));
    }

    @Test
    void buscarPorSkuDeveRetornarProduto() throws Exception {
        when(produtoService.buscarPorSku(SKU)).thenReturn(retorno);

        mockMvc.perform(get(URL + "/sku/{sku}", SKU))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));
    }

    @Test
    void atualizarDeveRetornar200() throws Exception {
        when(produtoService.atualizar(eq(ID), any())).thenReturn(retorno);

        mockMvc.perform(patch(URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\": 25.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoService).atualizar(eq(ID), captor.capture());
        assertThat(captor.getValue().valor()).isEqualByComparingTo(new BigDecimal("25.00"));
        assertThat(captor.getValue().sku()).isNull();
    }

    @Test
    void apagarDeveRetornar204() throws Exception {
        mockMvc.perform(delete(URL + "/{id}", ID))
                .andExpect(status().isNoContent());

        verify(produtoService).apagar(ID);
    }
}
