package br.com.devsombrero.logistica.domain.model;

import java.util.List;

public record Paginado<T>(
        List<T> conteudo,
        int paginaAtual,
        int tamanhoPagina,
        int totalPaginas,
        long totalElementos
) {
}
