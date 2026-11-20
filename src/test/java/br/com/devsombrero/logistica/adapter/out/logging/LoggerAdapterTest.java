package br.com.devsombrero.logistica.adapter.out.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class LoggerAdapterTest {

    private final LoggerAdapter adapter = new LoggerAdapter();
    private Logger logger;
    private Level nivelOriginal;
    private ListAppender<ILoggingEvent> appender;

    @BeforeEach
    void setUp() {
        logger = (Logger) LoggerFactory.getLogger(LoggerAdapterTest.class);
        nivelOriginal = logger.getLevel();
        logger.setLevel(Level.DEBUG);
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(appender);
        logger.setLevel(nivelOriginal);
    }

    @Test
    void deveRegistrarEmCadaNivel() {
        RuntimeException erro = new RuntimeException("causa");

        adapter.info(LoggerAdapterTest.class, "msg info");
        adapter.warn(LoggerAdapterTest.class, "msg warn");
        adapter.error(LoggerAdapterTest.class, "msg error");
        adapter.error(LoggerAdapterTest.class, "msg error throwable", erro);
        adapter.debug(LoggerAdapterTest.class, "msg debug");

        assertThat(appender.list)
                .extracting(ILoggingEvent::getLevel, ILoggingEvent::getFormattedMessage)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple(Level.INFO, "msg info"),
                        org.assertj.core.groups.Tuple.tuple(Level.WARN, "msg warn"),
                        org.assertj.core.groups.Tuple.tuple(Level.ERROR, "msg error"),
                        org.assertj.core.groups.Tuple.tuple(Level.ERROR, "msg error throwable"),
                        org.assertj.core.groups.Tuple.tuple(Level.DEBUG, "msg debug")
                );
        assertThat(appender.list.get(3).getThrowableProxy().getMessage()).isEqualTo("causa");
    }
}
