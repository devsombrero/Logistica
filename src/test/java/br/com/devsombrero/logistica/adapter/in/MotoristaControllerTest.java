package br.com.devsombrero.logistica.adapter.in;

import br.com.devsombrero.logistica.adapter.in.rest.motorista.MotoristaController;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.service.MotoristaService;
import br.com.devsombrero.logistica.support.MotoristaFixture;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MotoristaController.class)
class MotoristaControllerTest {

    private static final String URL = "/v1/motoristas";
    private static final String ID = MotoristaFixture.ID;
    private static final String CPF = MotoristaFixture.CPF;
    private static final String CNH = MotoristaFixture.CNH;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MotoristaService motoristaService;

    private final RetornoMotorista retorno = MotoristaFixture.retornoMotorista();

    @Test
    void buscarPaginadoDeveUsarValoresPadrao() throws Exception {
        when(motoristaService.buscarPaginado(0, 10))
                .thenReturn(new Paginado<>(List.of(retorno), 0, 10, 1, 1L));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(1)))
                .andExpect(jsonPath("$.conteudo[0].cpf").value(CPF))
                .andExpect(jsonPath("$.paginaAtual").value(0))
                .andExpect(jsonPath("$.tamanhoPagina").value(10))
                .andExpect(jsonPath("$.totalPaginas").value(1))
                .andExpect(jsonPath("$.totalElementos").value(1));
    }

    @Test
    void buscarPaginadoDeveRepassarParametros() throws Exception {
        when(motoristaService.buscarPaginado(2, 5))
                .thenReturn(new Paginado<>(List.of(), 2, 5, 0, 0L));

        mockMvc.perform(get(URL).param("tamanhoPagina", "5").param("paginaAtual", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", hasSize(0)));
    }

    @Test
    void cadastrarDeveRetornar201() throws Exception {
        when(motoristaService.cadastrar(any())).thenReturn(retorno);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MotoristaFixture.jsonValido()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.cnh").value(CNH));

        ArgumentCaptor<Motorista> captor = ArgumentCaptor.forClass(Motorista.class);
        verify(motoristaService).cadastrar(captor.capture());
        assertThat(captor.getValue()).isEqualTo(MotoristaFixture.novoMotorista());
    }

    @Test
    void cadastrarComCamposInvalidosDeveRetornar400ComDetalhes() throws Exception {
        String body = MotoristaFixture.json("", " ", "", null, null, null);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Erro de validação dos campos da requisição"))
                .andExpect(jsonPath("$.detalhes", hasSize(6)))
                .andExpect(jsonPath("$.detalhes", hasItems(
                        "nome: Nome é obrigatório",
                        "cpf: CPF é obrigatório",
                        "telefone: Telefone é obrigatório",
                        "cnh: CNH é obrigatória",
                        "validadeCnh: Validade da CNH é obrigatória",
                        "status: Status do motorista é obrigatório"
                )));

        verifyNoInteractions(motoristaService);
    }

    @Test
    void cadastrarDuplicadoDeveRetornar422() throws Exception {
        when(motoristaService.cadastrar(any()))
                .thenThrow(new EntidadeUnprocessableException("O motorista com esse cpf já existe na base de dados!"));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MotoristaFixture.jsonValido()))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.mensagem").value("O motorista com esse cpf já existe na base de dados!"));
    }

    @Test
    void buscarPorIdDeveRetornarMotorista() throws Exception {
        when(motoristaService.buscarPorId(ID)).thenReturn(retorno);

        mockMvc.perform(get(URL + "/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void buscarPorIdInexistenteDeveRetornar404() throws Exception {
        when(motoristaService.buscarPorId("x"))
                .thenThrow(new EntidadeNotFoundException("Motorista não encontrado!"));

        mockMvc.perform(get(URL + "/{id}", "x"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Motorista não encontrado!"))
                .andExpect(jsonPath("$.path").value(URL + "/x"));
    }

    @Test
    void buscarPorCnhDeveRetornarMotorista() throws Exception {
        when(motoristaService.buscarPorCnh(CNH)).thenReturn(retorno);

        mockMvc.perform(get(URL + "/cnh").param("cnh", CNH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));
    }

    @Test
    void buscarPorCpfDeveRetornarMotorista() throws Exception {
        when(motoristaService.buscarPorCpf(CPF)).thenReturn(retorno);

        mockMvc.perform(get(URL + "/cpf").param("cpf", CPF))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));
    }

    @Test
    void atualizarDeveRetornar200() throws Exception {
        when(motoristaService.atualizar(eq(ID), any())).thenReturn(retorno);

        mockMvc.perform(patch(URL + "/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"FERIAS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID));

        ArgumentCaptor<Motorista> captor = ArgumentCaptor.forClass(Motorista.class);
        verify(motoristaService).atualizar(eq(ID), captor.capture());
        assertThat(captor.getValue().status()).isEqualTo(StatusMotorista.FERIAS);
        assertThat(captor.getValue().cpf()).isNull();
    }

    @Test
    void apagarDeveRetornar204() throws Exception {
        mockMvc.perform(delete(URL + "/{id}", ID))
                .andExpect(status().isNoContent());

        verify(motoristaService).apagar(ID);
    }
}
