package br.com.devsombrero.logistica.domain.model;

import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Motorista(
        String id,
        String nome,
        String cpf,
        String telefone,
        String cnh,
        LocalDate validadeCnh,
        StatusMotorista status,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
    public Motorista(
            String nome,
            String cpf,
            String telefone,
            String cnh,
            LocalDate validadeCnh,
            StatusMotorista status
    ) {
        this(
                null,
                nome,
                cpf,
                telefone,
                cnh,
                validadeCnh,
                status,
                null,
                null
        );
    }
}
