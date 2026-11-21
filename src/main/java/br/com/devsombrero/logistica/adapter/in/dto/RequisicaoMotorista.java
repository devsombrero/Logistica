
package br.com.devsombrero.logistica.adapter.in.dto;

import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RequisicaoMotorista(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotBlank(message = "CNH é obrigatória")
        String cnh,

        @NotNull(message = "Validade da CNH é obrigatória")
        LocalDate validadeCnh,

        @NotNull(message = "Status do motorista é obrigatório")
        StatusMotorista status

) {

    public Motorista toDomain() {
        return new Motorista(
                nome,
                cpf,
                telefone,
                cnh,
                validadeCnh,
                status
        );
    }
}
