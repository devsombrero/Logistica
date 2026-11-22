package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.motorista.MotoristaDocument;
import br.com.devsombrero.logistica.domain.enumerator.StatusMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.support.MotoristaFixture;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MotoristaDocumentTest {

    @Test
    void fromDomainEToDomainDevemSerSimetricos() {
        Motorista motorista = MotoristaFixture.motoristaSalvo();

        MotoristaDocument document = MotoristaDocument.fromDomain(motorista);

        assertThat(document.id()).isEqualTo(motorista.id());
        assertThat(document.dataCriacao()).isEqualTo(motorista.dataCriacao());
        assertThat(MotoristaDocument.toDomain(document)).isEqualTo(motorista);
    }

    @Test
    void atualizarDeveSubstituirApenasValoresPreenchidos() {
        MotoristaDocument original = MotoristaDocument.fromDomain(MotoristaFixture.motoristaSalvo());
        LocalDate novaValidade = LocalDate.of(2030, 1, 1);
        Motorista alteracao = new Motorista(
                "Maria",
                null,
                "  ",
                "11111111111",
                novaValidade,
                null
        );

        MotoristaDocument atualizado = original.atualizar(alteracao);

        assertThat(atualizado.id()).isEqualTo(original.id());
        assertThat(atualizado.nome()).isEqualTo("Maria");
        assertThat(atualizado.cpf()).isEqualTo(original.cpf());
        assertThat(atualizado.telefone()).isEqualTo(original.telefone());
        assertThat(atualizado.cnh()).isEqualTo("11111111111");
        assertThat(atualizado.validadeCnh()).isEqualTo(novaValidade);
        assertThat(atualizado.status()).isEqualTo(original.status());
        assertThat(atualizado.dataCriacao()).isEqualTo(original.dataCriacao());
        assertThat(atualizado.dataAtualizacao()).isEqualTo(original.dataAtualizacao());
    }

    @Test
    void atualizarDeveManterValidadeQuandoNulaESubstituirStatus() {
        MotoristaDocument original = MotoristaDocument.fromDomain(MotoristaFixture.motoristaSalvo());
        Motorista alteracao = new Motorista(null, null, null, null, null, StatusMotorista.AFASTADO);

        MotoristaDocument atualizado = original.atualizar(alteracao);

        assertThat(atualizado.validadeCnh()).isEqualTo(original.validadeCnh());
        assertThat(atualizado.status()).isEqualTo(StatusMotorista.AFASTADO);
    }
}
