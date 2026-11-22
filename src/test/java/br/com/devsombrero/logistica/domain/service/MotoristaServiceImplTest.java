package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.motorista.ApagarMotoristaInputPort;
import br.com.devsombrero.logistica.application.port.in.motorista.AtualizarMotoristaInputPort;
import br.com.devsombrero.logistica.application.port.in.motorista.BuscarMotoristaInputPort;
import br.com.devsombrero.logistica.application.port.in.motorista.BuscarMotoristaPaginadoInputPort;
import br.com.devsombrero.logistica.application.port.in.motorista.CadastrarMotoristaInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.support.MotoristaFixture;
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
class MotoristaServiceImplTest {

    @Mock
    private CadastrarMotoristaInputPort cadastrar;
    @Mock
    private BuscarMotoristaInputPort buscar;
    @Mock
    private AtualizarMotoristaInputPort atualizar;
    @Mock
    private BuscarMotoristaPaginadoInputPort buscarPaginado;
    @Mock
    private ApagarMotoristaInputPort apagar;

    @InjectMocks
    private MotoristaServiceImpl service;

    private final RetornoMotorista retorno = MotoristaFixture.retornoMotorista();
    private final Motorista motorista = MotoristaFixture.novoMotorista();

    @Test
    void buscarPaginadoDeveDelegar() {
        Paginado<RetornoMotorista> paginado = new Paginado<>(List.of(retorno), 0, 10, 1, 1L);
        when(buscarPaginado.buscarPaginado(0, 10)).thenReturn(paginado);

        assertThat(service.buscarPaginado(0, 10)).isSameAs(paginado);
    }

    @Test
    void cadastrarDeveDelegar() {
        when(cadastrar.cadastrar(motorista)).thenReturn(retorno);

        assertThat(service.cadastrar(motorista)).isSameAs(retorno);
    }

    @Test
    void buscarPorIdDeveDelegar() {
        when(buscar.buscarPorId("1")).thenReturn(retorno);

        assertThat(service.buscarPorId("1")).isSameAs(retorno);
    }

    @Test
    void buscarPorCnhDeveDelegar() {
        when(buscar.buscarPorCnh("c")).thenReturn(retorno);

        assertThat(service.buscarPorCnh("c")).isSameAs(retorno);
    }

    @Test
    void buscarPorCpfDeveDelegar() {
        when(buscar.buscarPorCpf("p")).thenReturn(retorno);

        assertThat(service.buscarPorCpf("p")).isSameAs(retorno);
    }

    @Test
    void atualizarDeveDelegar() {
        when(atualizar.atualizar("1", motorista)).thenReturn(retorno);

        assertThat(service.atualizar("1", motorista)).isSameAs(retorno);
    }

    @Test
    void apagarDeveDelegar() {
        service.apagar("1");

        verify(apagar).apagar("1");
    }
}
