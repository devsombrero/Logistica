package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.veiculo.ApagarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.in.veiculo.AtualizarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.in.veiculo.BuscarVeiculoInputPort;
import br.com.devsombrero.logistica.application.port.in.veiculo.BuscarVeiculoPaginadoInputPort;
import br.com.devsombrero.logistica.application.port.in.veiculo.CadastrarVeiculoInputPort;
import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Veiculo;
import br.com.devsombrero.logistica.support.VeiculoFixture;
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
class VeiculoServiceImplTest {

    @Mock
    private BuscarVeiculoPaginadoInputPort buscarPaginado;
    @Mock
    private CadastrarVeiculoInputPort cadastrar;
    @Mock
    private BuscarVeiculoInputPort buscar;
    @Mock
    private AtualizarVeiculoInputPort atualizar;
    @Mock
    private ApagarVeiculoInputPort apagar;

    @InjectMocks
    private VeiculoServiceImpl service;

    private final RetornoVeiculo retorno = VeiculoFixture.retornoVeiculo();
    private final Veiculo veiculo = VeiculoFixture.novoVeiculo();

    @Test
    void buscarVeiculoPaginadoDeveDelegar() {
        FiltroPesquisaVeiculo filtro = new FiltroPesquisaVeiculo("FH", null, null, null);
        Paginado<RetornoVeiculo> paginado = new Paginado<>(List.of(retorno), 0, 10, 1, 1L);
        when(buscarPaginado.buscarPaginado(0, 10, filtro)).thenReturn(paginado);

        assertThat(service.buscarVeiculoPaginado(0, 10, filtro)).isSameAs(paginado);
    }

    @Test
    void cadastrarDeveDelegar() {
        when(cadastrar.cadastrar(veiculo)).thenReturn(retorno);

        assertThat(service.cadastrar(veiculo)).isSameAs(retorno);
    }

    @Test
    void buscarPorIdDeveDelegar() {
        when(buscar.buscarPorId("1")).thenReturn(retorno);

        assertThat(service.buscarPorId("1")).isSameAs(retorno);
    }

    @Test
    void buscarPorPlacaDeveDelegar() {
        when(buscar.buscarPorPlaca("p")).thenReturn(retorno);

        assertThat(service.buscarPorPlaca("p")).isSameAs(retorno);
    }

    @Test
    void atualizarDeveDelegar() {
        when(atualizar.atualizar("1", veiculo)).thenReturn(retorno);

        assertThat(service.atualizar("1", veiculo)).isSameAs(retorno);
    }

    @Test
    void apagarDeveDelegar() {
        service.apagar("1");

        verify(apagar).apagar("1");
    }
}
