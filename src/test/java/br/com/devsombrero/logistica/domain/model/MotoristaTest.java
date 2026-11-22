package br.com.devsombrero.logistica.domain.model;

import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MotoristaTest {

    @Test
    void construtorSemIdDeveDeixarIdEDatasNulos() {
        LocalDate validade = LocalDate.of(2030, 5, 10);
        Motorista motorista = new Motorista("Nome", "CPF", "Tel", "CNH", validade, StatusMotorista.ATIVO);

        assertThat(motorista.id()).isNull();
        assertThat(motorista.dataCriacao()).isNull();
        assertThat(motorista.dataAtualizacao()).isNull();
        assertThat(motorista.nome()).isEqualTo("Nome");
        assertThat(motorista.cpf()).isEqualTo("CPF");
        assertThat(motorista.telefone()).isEqualTo("Tel");
        assertThat(motorista.cnh()).isEqualTo("CNH");
        assertThat(motorista.validadeCnh()).isEqualTo(validade);
        assertThat(motorista.status()).isEqualTo(StatusMotorista.ATIVO);
    }
}
