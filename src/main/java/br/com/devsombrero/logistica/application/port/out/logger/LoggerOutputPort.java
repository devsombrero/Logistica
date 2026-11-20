package br.com.devsombrero.logistica.application.port.out.logger;

public interface LoggerOutputPort {
    void info(Class<?> source, String message);
    void warn(Class<?> source, String message);
    void error(Class<?> source, String message);
    void error(Class<?> source, String message, Throwable throwable);
    void debug(Class<?> source, String message);
}
