package br.com.devsombrero.logistica.domain.dto;

public record RetornoCliente(
        String id,
        String nome,
        String documento,
        String telefone,
        String email,
        String endereco,
        String cidade,
        String estado
) {
}
