package br.com.devsombrero.logistica.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionsTest {

    @Test
    void entidadeNotFoundDeveManterMensagem() {
        assertThat(new EntidadeNotFoundException("nf"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("nf");
    }

    @Test
    void entidadeUnprocessableDeveManterMensagem() {
        assertThat(new EntidadeUnprocessableException("up"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("up");
    }

    @Test
    void requisicaoInvalidaDeveManterMensagem() {
        assertThat(new RequisicaoInvalidaException("ri"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("ri");
    }
}
