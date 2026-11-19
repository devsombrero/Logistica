package br.com.devsombrero.logistica.domain.model;

import java.time.LocalDateTime;

public record Cliente(
   String id,
   String nome,
   String documento,
   String telefone,
   String email,
   String endereco,
   String cidade,
   String estado,
   LocalDateTime dataCriacao,
   LocalDateTime dataAtualizacao
) {

    public Cliente(
            String nome,
            String documento,
            String telefone,
            String email,
            String endereco,
            String cidade,
            String estado
    ) {
        this(null, nome, documento, telefone, email,
                endereco, cidade, estado, null, null);
    }
}
