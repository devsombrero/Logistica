package br.com.devsombrero.logistica.application.usecase;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import br.com.devsombrero.logistica.application.port.out.produto.ProdutoPersistenceOutputPort;
import br.com.devsombrero.logistica.application.usecase.produto.ApagarProdutoUseCase;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.support.ProdutoFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApagarProdutoUseCaseTest {

    @Mock
    private ProdutoPersistenceOutputPort persistence;
    @Mock
    private LoggerOutputPort logger;

    @InjectMocks
    private ApagarProdutoUseCase useCase;

    @Test
    void deveApagarProduto() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.of(ProdutoFixture.produtoSalvo()));

        useCase.apagar("1");

        verify(persistence).deletar("1");
        verify(logger).info(ApagarProdutoUseCase.class, "Produto excluído com sucesso");
    }

    @Test
    void deveLancarQuandoProdutoNaoExiste() {
        when(persistence.buscarPorId("1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.apagar("1"))
                .isInstanceOf(EntidadeNotFoundException.class)
                .hasMessage("Produto não encontrado com esse ID");

        verify(persistence, never()).deletar(anyString());
        verify(logger).warn(ApagarProdutoUseCase.class, "Produto não encontrado para exclusão");
    }
}
