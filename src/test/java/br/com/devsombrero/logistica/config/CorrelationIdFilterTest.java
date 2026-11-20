package br.com.devsombrero.logistica.config;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @Test
    void deveReaproveitarCorrelationIdRecebido() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CorrelationIdFilter.HEADER_NAME, "abc-123");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicReference<String> mdcDuranteChain = new AtomicReference<>();

        FilterChain chain = (req, res) -> mdcDuranteChain.set(MDC.get(CorrelationIdFilter.CORRELATION_ID));

        filter.doFilter(request, response, chain);

        assertThat(mdcDuranteChain.get()).isEqualTo("abc-123");
        assertThat(response.getHeader(CorrelationIdFilter.HEADER_NAME)).isEqualTo("abc-123");
        assertThat(MDC.get(CorrelationIdFilter.CORRELATION_ID)).isNull();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void deveGerarCorrelationIdQuandoAusenteOuEmBranco(String header) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        if (header != null) {
            request.addHeader(CorrelationIdFilter.HEADER_NAME, header);
        }
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicReference<String> mdcDuranteChain = new AtomicReference<>();

        filter.doFilter(request, response,
                (req, res) -> mdcDuranteChain.set(MDC.get(CorrelationIdFilter.CORRELATION_ID)));

        String gerado = response.getHeader(CorrelationIdFilter.HEADER_NAME);
        assertThat(UUID.fromString(gerado)).isNotNull();
        assertThat(mdcDuranteChain.get()).isEqualTo(gerado);
        assertThat(MDC.get(CorrelationIdFilter.CORRELATION_ID)).isNull();
    }

    @Test
    void deveLimparMdcMesmoQuandoChainLancaExcecao() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {
            throw new IllegalStateException("erro");
        };

        assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                .isInstanceOf(IllegalStateException.class);
        assertThat(MDC.get(CorrelationIdFilter.CORRELATION_ID)).isNull();
    }
}
