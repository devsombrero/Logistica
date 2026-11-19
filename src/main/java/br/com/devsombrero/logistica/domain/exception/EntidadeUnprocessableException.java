package br.com.devsombrero.logistica.domain.exception;

public class EntidadeUnprocessableException extends RuntimeException {
    public EntidadeUnprocessableException(String message) {
        super(message);
    }
}
