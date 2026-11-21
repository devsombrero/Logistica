package br.com.devsombrero.logistica.domain.dto;

import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;

import java.time.LocalDate;

public record RetornoMotorista(
        String id,
        String nome,
        String cpf,
        String telefone,
        String cnh,
        LocalDate validadeCnh,
        StatusMotorista status
) {
}
