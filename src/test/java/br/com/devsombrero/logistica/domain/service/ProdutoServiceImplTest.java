package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.produto.ApagarProdutoInputPort;
import br.com.devsombrero.logistica.application.port.in.produto.AtualizarProdutoInputPort;
import br.com.devsombrero.logistica.application.port.in.produto.BuscarProdutoInputPort;
import br.com.devsombrero.logistica.application.port.in.produto.BuscarProdutoPaginadoInputPort;
import br.com.devsombrero.logistica.application.port.in.produto.CadastrarProdutoInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Produto;
import br.com.devsombrero.logistica.support.ProdutoFixture;
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
class ProdutoServiceImplTest {

    @Mock
    private BuscarProdutoPaginadoInputPort buscarPaginado;
    @Mock
    private CadastrarProdutoInputPort cadastrar;
    @Mock
    private BuscarProdutoInputPort buscar;
    @Mock
    private AtualizarProdutoInputPort atualizar;
    @Mock
    private ApagarProdutoInputPort apagar;

    @InjectMocks
    private ProdutoServiceImpl service;

    private final RetornoProduto retorno = ProdutoFixture.retornoProduto();
    private final Produto produto = ProdutoFixture.novoProduto();

    @Test
    void buscarPaginadoDeveDelegar() {
        Paginado<RetornoProduto> paginado = new Paginado<>(List.of(retorno), 0, 10, 1, 1L);
        when(buscarPaginado.buscarPaginado(10, 0)).thenReturn(paginado);

        assertThat(service.buscarPaginado(10, 0)).isSameAs(paginado);
    }

    @Test
    void cadastrarDeveDelegar() {
        when(cadastrar.cadastrar(produto)).thenReturn(retorno);

        assertThat(service.cadastrar(produto)).isSameAs(retorno);
    }

    @Test
    void buscarPorIdDeveDelegar() {
        when(buscar.buscarPorId("1")).thenReturn(retorno);

        assertThat(service.buscarPorId("1")).isSameAs(retorno);
    }

    @Test
    void buscarPorSkuDeveDelegar() {
        when(buscar.buscarPorSku("s")).thenReturn(retorno);

        assertThat(service.buscarPorSku("s")).isSameAs(retorno);
    }

    @Test
    void atualizarDeveDelegar() {
        when(atualizar.atualizar("1", produto)).thenReturn(retorno);

        assertThat(service.atualizar("1", produto)).isSameAs(retorno);
    }

    @Test
    void apagarDeveDelegar() {
        service.apagar("1");

        verify(apagar).apagar("1");
    }
}
