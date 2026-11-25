package br.com.devsombrero.logistica.domain.model;

import java.util.List;
import java.util.function.Function;

public record Paginado<T>(
        List<T> conteudo,
        int paginaAtual,
        int tamanhoPagina,
        int totalPaginas,
        long totalElementos
) {
    public <R> Paginado<R> map(Function<T, R> conversor) {
        return new Paginado<>(
                conteudo.stream().map(conversor).toList(),
                paginaAtual,
                tamanhoPagina,
                (int) totalElementos,
                totalPaginas
        );
    }
}
