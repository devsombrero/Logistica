package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.pedido.AtualizarPedidoInputPort;
import br.com.devsombrero.logistica.application.port.in.pedido.BuscarPedidoInputPort;
import br.com.devsombrero.logistica.application.port.in.pedido.BuscarPedidoPaginadoInputPort;
import br.com.devsombrero.logistica.application.port.in.pedido.CadastrarPedidoInputPort;
import br.com.devsombrero.logistica.application.port.in.pedido.CancelarPedidoInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Pedido;
import br.com.devsombrero.logistica.support.PedidoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private BuscarPedidoInputPort buscar;
    @Mock
    private CadastrarPedidoInputPort cadastrar;
    @Mock
    private AtualizarPedidoInputPort atualizar;
    @Mock
    private CancelarPedidoInputPort cancelar;
    @Mock
    private BuscarPedidoPaginadoInputPort buscarPaginado;

    @InjectMocks
    private PedidoServiceImpl service;

    private final RetornoPedido retorno = PedidoFixture.retornoPedido();
    private final Pedido pedido = PedidoFixture.requisicaoCadastro();
    private final Paginado<RetornoPedido> paginado = new Paginado<>(List.of(retorno), 0, 10, 1, 1L);

    @Test
    void buscarPaginadoDeveDelegar() {
        when(buscarPaginado.buscarPaginado(10, 0)).thenReturn(paginado);

        assertThat(service.buscarPaginado(10, 0)).isSameAs(paginado);
    }

    @Test
    void buscarPorClienteDeveDelegar() {
        when(buscarPaginado.buscarPorCliente("c", 10, 0)).thenReturn(paginado);

        assertThat(service.buscarPorCliente("c", 10, 0)).isSameAs(paginado);
    }

    @Test
    void buscarPorStatusDeveDelegar() {
        when(buscarPaginado.buscarPorStatus(StatusPedido.APROVADO, 10, 0)).thenReturn(paginado);

        assertThat(service.buscarPorStatus(StatusPedido.APROVADO, 10, 0)).isSameAs(paginado);
    }

    @Test
    void cadastrarDeveDelegar() {
        when(cadastrar.cadastrar(pedido)).thenReturn(retorno);

        assertThat(service.cadastrar(pedido)).isSameAs(retorno);
    }

    @Test
    void atualizarDeveDelegar() {
        when(atualizar.atualizar("1", pedido)).thenReturn(retorno);

        assertThat(service.atualizar("1", pedido)).isSameAs(retorno);
    }

    @Test
    void cancelarDeveDelegar() {
        when(cancelar.cancelar("1")).thenReturn(retorno);

        assertThat(service.cancelar("1")).isSameAs(retorno);
    }

    @Test
    void buscarPorIdDeveDelegar() {
        when(buscar.buscarPorId("1")).thenReturn(retorno);

        assertThat(service.buscarPorId("1")).isSameAs(retorno);
    }
}
