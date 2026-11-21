package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.produto.CadastrarProdutoUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeUnprocessableException;
import br.com.devsombrero.logistica.domain.exception.RequisicaoInvalidaException;
import br.com.devsombrero.logistica.domain.model.Produto;
import br.com.devsombrero.logistica.support.ProdutoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarProdutoUseCaseTest {

    @Mock
    private ProdutoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private CadastrarProdutoUseCase useCase;

    @Test
    void deveCadastrarProduto() {
        Produto novo = ProdutoFixture.novoProduto();
        when(persistence.existePorSku(novo.sku())).thenReturn(false);
        when(persistence.salvar(novo)).thenReturn(ProdutoFixture.produtoSalvo());

        assertThat(useCase.cadastrar(novo)).isEqualTo(ProdutoFixture.retornoProduto());
        verify(logger).info(CadastrarProdutoUseCase.class, "Produto cadastrado com sucesso");
    }

    @Test
    void deveRecusarProdutoNulo() {
        assertThatThrownBy(() -> useCase.cadastrar(null))
                .isInstanceOf(RequisicaoInvalidaException.class)
                .hasMessage("O produto não pode ser nulo!");

        verifyNoInteractions(persistence);
        verify(logger).warn(CadastrarProdutoUseCase.class,
                "Cadastro de produto recusado: Produto não pode ser null!");
    }

    @Test
    void deveRecusarSkuDuplicado() {
        Produto novo = ProdutoFixture.novoProduto();
        when(persistence.existePorSku(novo.sku())).thenReturn(true);

        assertThatThrownBy(() -> useCase.cadastrar(novo))
                .isInstanceOf(EntidadeUnprocessableException.class)
                .hasMessage("O produto com esse sku já existe na base de dados!");

        verify(persistence, never()).salvar(any());
        verify(logger).warn(CadastrarProdutoUseCase.class,
                "Cadastro de produto recusado: Produto com sku já existe!");
    }
}
