package br.com.devsombrero.logistica.adapter.out.logging;

import br.com.devsombrero.logistica.application.port.out.logger.LoggerOutputPort;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerAdapter implements LoggerOutputPort {

    @Override
    public void info(Class<?> source, String message) {
        LoggerFactory.getLogger(source).info(message);
    }

    @Override
    public void warn(Class<?> source, String message) {
        LoggerFactory.getLogger(source).warn(message);
    }

    @Override
    public void error(Class<?> source, String message) {
        LoggerFactory.getLogger(source).error(message);
    }

    @Override
    public void error(Class<?> source, String message, Throwable throwable) {
        LoggerFactory.getLogger(source).error(message, throwable);
    }

    @Override
    public void debug(Class<?> source, String message) {
        LoggerFactory.getLogger(source).debug(message);
    }
}