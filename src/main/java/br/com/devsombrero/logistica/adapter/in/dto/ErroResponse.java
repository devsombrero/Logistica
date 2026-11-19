package br.com.devsombrero.logistica.adapter.in.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(
        LocalDateTime timestamp,
        Integer status,
        String erro,
        String mensagem,
        String path,
        List<String> detalhes
) {
}
