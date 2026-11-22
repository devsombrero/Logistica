package br.com.devsombrero.logistica.support;

import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class MotoristaFixture {

    public static final String ID = "885f1c2e9b1e8a3d4c5b6a8f";
    public static final String CPF = "12345678901";
    public static final String CNH = "98765432100";
    public static final LocalDate VALIDADE_CNH = LocalDate.now().plusYears(2);
    public static final LocalDateTime DATA = LocalDateTime.of(2026, 2, 15, 9, 45);

    private MotoristaFixture() {
    }

    public static Motorista novoMotorista() {
        return novoMotorista(VALIDADE_CNH);
    }

    public static Motorista novoMotorista(LocalDate validadeCnh) {
        return new Motorista(
                "João Silva",
                CPF,
                "11999999999",
                CNH,
                validadeCnh,
                StatusMotorista.ATIVO
        );
    }

    public static Motorista motoristaSalvo() {
        return new Motorista(
                ID,
                "João Silva",
                CPF,
                "11999999999",
                CNH,
                VALIDADE_CNH,
                StatusMotorista.ATIVO,
                DATA,
                DATA
        );
    }

    public static RetornoMotorista retornoMotorista() {
        return new RetornoMotorista(
                ID,
                "João Silva",
                CPF,
                "11999999999",
                CNH,
                VALIDADE_CNH,
                StatusMotorista.ATIVO
        );
    }

    public static String json(String nome, String cpf, String telefone, String cnh,
                              String validadeCnh, String status) {
        return """
                {
                  "nome": %s,
                  "cpf": %s,
                  "telefone": %s,
                  "cnh": %s,
                  "validadeCnh": %s,
                  "status": %s
                }
                """.formatted(q(nome), q(cpf), q(telefone), q(cnh), q(validadeCnh), q(status));
    }

    public static String jsonValido() {
        return json("João Silva", CPF, "11999999999", CNH, VALIDADE_CNH.toString(), "ATIVO");
    }

    private static String q(String valor) {
        return valor == null ? "null" : "\"" + valor + "\"";
    }
}
