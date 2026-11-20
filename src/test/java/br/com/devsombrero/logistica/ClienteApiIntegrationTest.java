package br.com.devsombrero.logistica;

import br.com.devsombrero.logistica.adapter.out.persistence.ClienteMongoRepository;
import br.com.devsombrero.logistica.config.CorrelationIdFilter;
import br.com.devsombrero.logistica.support.ClienteFixture;
import br.com.devsombrero.logistica.support.TestcontainersConfiguration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ClienteApiIntegrationTest {

    private static final String URL = "/v1/clientes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteMongoRepository repository;

    @BeforeEach
    void limpar() {
        repository.deleteAll();
    }

    @Test
    void cicloCompletoDoCliente() throws Exception {
        String body = mockMvc.perform(post(URL)
                        .header(CorrelationIdFilter.HEADER_NAME, "teste-e2e")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClienteFixture.jsonValido()))
                .andExpect(status().isCreated())
                .andExpect(header().string(CorrelationIdFilter.HEADER_NAME, "teste-e2e"))
                .andReturn().getResponse().getContentAsString();
        String id = JsonPath.read(body, "$.id");

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClienteFixture.jsonValido()))
                .andExpect(status().isUnprocessableContent());

        mockMvc.perform(get(URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Silva"));

        mockMvc.perform(get(URL + "/email").param("email", "maria@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(get(URL + "/documento").param("documento", "12345678900"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(get(URL).param("tamanhoPagina", "10").param("paginaAtual", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(1)))
                .andExpect(jsonPath("$.paginaAtual").value(0))
                .andExpect(jsonPath("$.tamanhoPagina").value(10))
                .andExpect(jsonPath("$.totalPaginas").value(1))
                .andExpect(jsonPath("$.totalElementos").value(1));

        mockMvc.perform(patch(URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Maria Souza\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Souza"))
                .andExpect(jsonPath("$.email").value("maria@email.com"));

        mockMvc.perform(delete(URL + "/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(URL + "/{id}", id))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete(URL + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarComEmailDeOutroClienteDeveRetornar422() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClienteFixture.jsonValido()))
                .andExpect(status().isCreated());

        String body = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ClienteFixture.json("João", "999", "1188", "joao@email.com",
                                "Rua B", "Rio de Janeiro", "RJ")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String idJoao = JsonPath.read(body, "$.id");

        mockMvc.perform(patch(URL + "/{id}", idJoao)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"maria@email.com\"}"))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.mensagem").value("O e-mail ou documento informado já pertence a outro cliente."));
    }

    @Test
    void buscarPaginadoComParametrosInvalidosDeveRetornar400() throws Exception {
        mockMvc.perform(get(URL).param("tamanhoPagina", "0"))
                .andExpect(status().isBadRequest());
    }
}
