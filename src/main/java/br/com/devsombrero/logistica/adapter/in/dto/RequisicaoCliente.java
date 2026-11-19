package br.com.devsombrero.logistica.adapter.in.dto;

import br.com.devsombrero.logistica.domain.model.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "RequisicaoCliente", description = "Dados descritivos do Cliente")
public record RequisicaoCliente(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Documento é obrigatório")
        String documento,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Endereço é obrigatório")
        String endereco,

        @NotBlank(message = "Cidade é obrigatória")
        String cidade,

        @NotBlank(message = "Estado é obrigatório")
        String estado
) {
    public Cliente toDomain() {
        return new Cliente(
                nome,
                documento,
                telefone,
                email,
                endereco,
                cidade,
                estado
        );
    }
}
