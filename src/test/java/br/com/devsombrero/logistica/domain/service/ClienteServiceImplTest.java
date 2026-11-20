package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.ApagarClienteInputPort;
import br.com.devsombrero.logistica.application.port.in.AtualizarClienteInputPort;
import br.com.devsombrero.logistica.application.port.in.BuscarClienteInputPort;
import br.com.devsombrero.logistica.application.port.in.BuscarPaginadoInputPort;
import br.com.devsombrero.logistica.application.port.in.CadastrarClienteInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.support.ClienteFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private CadastrarClienteInputPort cadastrar;
    @Mock
    private BuscarClienteInputPort buscar;
    @Mock
    private AtualizarClienteInputPort atualizar;
    @Mock
    private ApagarClienteInputPort apagar;
    @Mock
    private BuscarPaginadoInputPort buscarPaginado;

    @InjectMocks
    private ClienteServiceImpl service;

    private final RetornoCliente retorno = ClienteFixture.retornoCliente();
    private final Cliente cliente = ClienteFixture.novoCliente();

    @Test
    void buscarPaginadoDeveDelegar() {
        Paginado<RetornoCliente> paginado = new Paginado<>(List.of(retorno), 0, 10, 1, 1L);
        when(buscarPaginado.buscarPaginado(10, 0)).thenReturn(paginado);

        assertThat(service.buscarPaginado(10, 0)).isSameAs(paginado);
    }

    @Test
    void cadastrarDeveDelegar() {
        when(cadastrar.cadastrar(cliente)).thenReturn(retorno);

        assertThat(service.cadastrar(cliente)).isSameAs(retorno);
    }

    @Test
    void buscarPorIdDeveDelegar() {
        when(buscar.buscarPorId("1")).thenReturn(retorno);

        assertThat(service.buscarPorId("1")).isSameAs(retorno);
    }

    @Test
    void buscarPorEmailDeveDelegar() {
        when(buscar.buscarPorEmail("e")).thenReturn(retorno);

        assertThat(service.buscarPorEmail("e")).isSameAs(retorno);
    }

    @Test
    void buscarPorDocumentoDeveDelegar() {
        when(buscar.buscarPorDocumento("d")).thenReturn(retorno);

        assertThat(service.buscarPorDocumento("d")).isSameAs(retorno);
    }

    @Test
    void atualizarDeveDelegar() {
        when(atualizar.atualizar("1", cliente)).thenReturn(retorno);

        assertThat(service.atualizar("1", cliente)).isSameAs(retorno);
    }

    @Test
    void apagarDeveDelegar() {
        service.apagar("1");

        verify(apagar).apagar("1");
    }
}
