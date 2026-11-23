package br.com.devsombrero.logistica.adapter.in;

import br.com.devsombrero.logistica.adapter.in.rest.cliente.ClienteController;
import br.com.devsombrero.logistica.config.CorrelationIdFilter;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.service.ClienteService;
import br.com.devsombrero.logistica.support.ClienteFixture;
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
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    private static final String URL = "/v1/clientes";
    private static final String ID = ClienteFixture.ID;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private final RetornoCliente retorno = ClienteFixture.retornoCliente();

    @Test
    void buscarPaginadoDeveUsarValoresPadrao() throws Exception {
        when(clienteService.buscarPaginado(10, 0))
                .thenReturn(new Paginado<>(List.of(retorno), 0, 10, 1, 1L));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(header().exists(CorrelationIdFilter.HEADER_NAME))
                .andExpect(jsonPath("$.conteudo", hasSize(1)))
                .andExpect(jsonPath("$.conteudo[0].id").value(ID))
                .andExpect(jsonPath("$.paginaAtual").value(0))
                .andExpect(jsonPath("$.tamanhoPagina").value(10))
                .andExpect(jsonPath("$.totalPaginas").value(1))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    void buscarPaginadoDeveRepassarParametros() throws Exception {
        when(clienteService.buscarPaginado(5, 2))
                .thenReturn(new Paginado<>(List.of(), 2, 5, 0, 0L));

        mockMvc.perform(get(URL).param("tamanhoPagina", "5").param("paginaAtual", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(0)));
    }

    @Test
    void buscarPaginadoComParametroInvalidoDeveRetornar400() throws Exception {
        when(clienteService.buscarPaginado(0, 0))
                .thenThrow(new RequisicaoInvalidaException("parâmetros inválidos"));

        mockMvc.perform(get(URL).param("tamanhoPagina", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.mensagem").value("parâmetros inválidos"))
                .andExpect(jsonPath("$.path").value(URL))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.detalhes", hasSize(0)));
    }

    @Test
    void buscarPaginadoComTipoInvalidoDeveRetornar400() throws Exception {
        mockMvc.perform(get(URL).param("tamanhoPagina", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem").value("Valor inválido para o parâmetro 'tamanhoPagina': abc"))
                .andExpect(jsonPath("$.path").value(URL));

        verifyNoInteractions(clienteService);
    }

    @Test
    void buscarPorEmailSemParametroDeveRetornar400() throws Exception {
        mockMvc.perform(get(URL + "/email"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Parâmetro obrigatório ausente: email"));

        verifyNoInteractions(clienteService);
    }

    @Test
    void cadastrarComJsonMalformadoDeveRetornar400() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Corpo da requisição ausente ou malformado"));

        verifyNoInteractions(clienteService);
    }

    @Test
    void cadastrarDeveRetornar201() throws Exception {
        when(clienteService.cadastrar(any())).thenReturn(retorno);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClienteFixture.jsonValido()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.nome").value("Maria Silva"));

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteService).cadastrar(captor.capture());
        assertThat(captor.getValue()).isEqualTo(ClienteFixture.novoCliente());
    }

    @Test
    void cadastrarComCamposInvalidosDeveRetornar400ComDetalhes() throws Exception {
        String body = ClienteFixture.json("", " ", null, "invalido", "", "", "");

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Erro de validação dos campos da requisição"))
                .andExpect(jsonPath("$.path").value(URL))
                .andExpect(jsonPath("$.detalhes", hasSize(7)))
                .andExpect(jsonPath("$.detalhes", hasItems(
                        "nome: Nome é obrigatório",
                        "documento: Documento é obrigatório",
                        "telefone: Telefone é obrigatório",
                        "email: E-mail inválido",
                        "endereco: Endereço é obrigatório",
                        "cidade: Cidade é obrigatória",
                        "estado: Estado é obrigatório"
                )));

        verifyNoInteractions(clienteService);
    }

    @Test
    void cadastrarDuplicadoDeveRetornar422() throws Exception {
        when(clienteService.cadastrar(any()))
                .thenThrow(new EntidadeUnprocessableException("Cliente já existe com esse e-mail/documento"));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClienteFixture.jsonValido()))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.mensagem").value("Cliente já existe com esse e-mail/documento"));
    }

    @Test
    void buscarPorIdDeveRetornarCliente() throws Exception {
        when(clienteService.buscarPorId(ID)).thenReturn(retorno);

        mockMvc.perform(get(URL + "/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    void buscarPorIdInexistenteDeveRetornar404() throws Exception {
        when(clienteService.buscarPorId("x"))
                .thenThrow(new EntidadeNotFoundException("Cliente não encontrado com esse ID"));

        mockMvc.perform(get(URL + "/{id}", "x"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Not Found"))
                .andExpect(jsonPath("$.mensagem").value("Cliente não encontrado com esse ID"))
                .andExpect(jsonPath("$.path").value(URL + "/x"));
    }

    @Test
    void buscarPorEmailDeveRetornarCliente() throws Exception {
        when(clienteService.buscarPorEmail("maria@email.com")).thenReturn(retorno);

        mockMvc.perform(get(URL + "/email").param("email", "maria@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));
    }

    @Test
    void buscarPorDocumentoDeveRetornarCliente() throws Exception {
        when(clienteService.buscarPorDocumento("12345678900")).thenReturn(retorno);

        mockMvc.perform(get(URL + "/documento").param("documento", "12345678900"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documento").value("12345678900"));
    }

    @Test
    void atualizarDeveRetornar200() throws Exception {
        when(clienteService.atualizar(eq(ID), any())).thenReturn(retorno);

        mockMvc.perform(patch(URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Maria Souza\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteService).atualizar(eq(ID), captor.capture());
        assertThat(captor.getValue().nome()).isEqualTo("Maria Souza");
        assertThat(captor.getValue().email()).isNull();
    }

    @Test
    void apagarDeveRetornar204() throws Exception {
        mockMvc.perform(delete(URL + "/{id}", ID))
                .andExpect(status().isNoContent());

        verify(clienteService).apagar(ID);
    }

    @Test
    void erroInesperadoDeveRetornar500() throws Exception {
        doThrow(new IllegalStateException("falha inesperada")).when(clienteService).apagar(ID);

        mockMvc.perform(delete(URL + "/{id}", ID))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.erro").value("Internal Server Error"))
                .andExpect(jsonPath("$.mensagem").value("falha inesperada"));
    }
}
