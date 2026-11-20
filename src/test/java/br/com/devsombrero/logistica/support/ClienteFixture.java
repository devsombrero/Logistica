package br.com.devsombrero.logistica.support;

import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Cliente;

import java.time.LocalDateTime;

public final class ClienteFixture {

    public static final String ID = "665f1c2e9b1e8a3d4c5b6a7f";
    public static final LocalDateTime DATA = LocalDateTime.of(2026, 1, 10, 12, 30);

    private ClienteFixture() {
    }

    public static Cliente novoCliente() {
        return new Cliente(
                "Maria Silva",
                "12345678900",
                "11999999999",
                "maria@email.com",
                "Rua A, 100",
                "São Paulo",
                "SP"
        );
    }

    public static Cliente clienteSalvo() {
        return new Cliente(
                ID,
                "Maria Silva",
                "12345678900",
                "11999999999",
                "maria@email.com",
                "Rua A, 100",
                "São Paulo",
                "SP",
                DATA,
                DATA
        );
    }

    public static RetornoCliente retornoCliente() {
        return new RetornoCliente(
                ID,
                "Maria Silva",
                "12345678900",
                "11999999999",
                "maria@email.com",
                "Rua A, 100",
                "São Paulo",
                "SP"
        );
    }

    public static String json(String nome, String documento, String telefone, String email,
                              String endereco, String cidade, String estado) {
        return """
                {
                  "nome": %s,
                  "documento": %s,
                  "telefone": %s,
                  "email": %s,
                  "endereco": %s,
                  "cidade": %s,
                  "estado": %s
                }
                """.formatted(q(nome), q(documento), q(telefone), q(email), q(endereco), q(cidade), q(estado));
    }

    public static String jsonValido() {
        return json("Maria Silva", "12345678900", "11999999999", "maria@email.com",
                "Rua A, 100", "São Paulo", "SP");
    }

    private static String q(String valor) {
        return valor == null ? "null" : "\"" + valor + "\"";
    }
}
