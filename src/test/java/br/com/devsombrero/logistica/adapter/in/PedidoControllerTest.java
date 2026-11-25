package br.com.devsombrero.logistica.adapter.in;

import br.com.devsombrero.logistica.adapter.in.rest.pedido.PedidoController;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.ItemPedido;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Pedido;
import br.com.devsombrero.logistica.domain.service.PedidoService;
import br.com.devsombrero.logistica.support.PedidoFixture;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    private static final String URL = "/v1/pedidos";
    private static final String ID = PedidoFixture.ID;
    private static final String CLIENTE_ID = PedidoFixture.CLIENTE_ID;
    private static final String PRODUTO_ID = PedidoFixture.PRODUTO_ID;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    private final RetornoPedido retorno = PedidoFixture.retornoPedido();
    private final Paginado<RetornoPedido> paginado = new Paginado<>(List.of(retorno), 0, 20, 1, 1L);

    @Test
    void cadastrarDeveRetornar201() throws Exception {
        when(pedidoService.cadastrar(any())).thenReturn(retorno);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clienteId": "%s",
                                  "observacao": "%s",
                                  "itens": [{"produtoId": "%s", "quantidade": 2}]
                                }
                                """.formatted(CLIENTE_ID, PedidoFixture.OBSERVACAO, PRODUTO_ID)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.statusPedido").value("PENDENTE"));

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoService).cadastrar(captor.capture());
        assertThat(captor.getValue()).isEqualTo(PedidoFixture.requisicaoCadastro());
    }

    @Test
    void cadastrarComCamposInvalidosDeveRetornar400() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\": \"\", \"itens\": []}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes", hasSize(2)))
                .andExpect(jsonPath("$.detalhes", hasItems(
                        "clienteId: O cliente é obrigatório",
                        "itens: O pedido deve possuir pelo menos um produto"
                )));

        verifyNoInteractions(pedidoService);
    }

    @Test
    void buscarPorIdDeveRetornarPedido() throws Exception {
        when(pedidoService.buscarPorId(ID)).thenReturn(retorno);

        mockMvc.perform(get(URL + "/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(CLIENTE_ID))
                .andExpect(jsonPath("$.itens", hasSize(1)));
    }

    @Test
    void buscarPorIdInexistenteDeveRetornar404() throws Exception {
        when(pedidoService.buscarPorId("x")).thenThrow(new EntidadeNotFoundException("Pedido x não encontrado"));

        mockMvc.perform(get(URL + "/{id}", "x"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Pedido x não encontrado"));
    }

    @Test
    void atualizarComItensDeveRepassarItens() throws Exception {
        when(pedidoService.atualizar(eq(ID), any())).thenReturn(retorno);

        mockMvc.perform(patch(URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"observacao\": \"nova\", \"itens\": [{\"produtoId\": \"%s\", \"quantidade\": 2}]}"
                                .formatted(PRODUTO_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoService).atualizar(eq(ID), captor.capture());
        assertThat(captor.getValue().observacao()).isEqualTo("nova");
        assertThat(captor.getValue().itens())
                .containsExactly(new ItemPedido(PRODUTO_ID, null, 2, null, null));
    }

    @Test
    void atualizarSemItensDeveEnviarListaVazia() throws Exception {
        when(pedidoService.atualizar(eq(ID), any())).thenReturn(retorno);

        mockMvc.perform(patch(URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"observacao\": \"nova\"}"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoService).atualizar(eq(ID), captor.capture());
        assertThat(captor.getValue().observacao()).isEqualTo("nova");
        assertThat(captor.getValue().itens()).isEmpty();
    }

    @Test
    void buscarPaginadoDeveUsarValoresPadrao() throws Exception {
        when(pedidoService.buscarPaginado(20, 0)).thenReturn(paginado);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(1)))
                .andExpect(jsonPath("$.tamanhoPagina").value(20))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    void buscarPorClienteDeveRepassarParametros() throws Exception {
        when(pedidoService.buscarPorCliente(CLIENTE_ID, 5, 2)).thenReturn(paginado);

        mockMvc.perform(get(URL + "/cliente/{clienteId}", CLIENTE_ID)
                        .param("tamanhoPagina", "5")
                        .param("paginaAtual", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo[0].id").value(ID));
    }

    @Test
    void buscarPorStatusDeveConverterStatus() throws Exception {
        when(pedidoService.buscarPorStatus(StatusPedido.APROVADO, 20, 0)).thenReturn(paginado);

        mockMvc.perform(get(URL + "/status/{status}", "APROVADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(1)));
    }

    @Test
    void cancelarDeveRetornar200() throws Exception {
        when(pedidoService.cancelar(ID)).thenReturn(retorno);

        mockMvc.perform(patch(URL + "/{id}/cancelar", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));
    }
}
