package br.com.devsombrero.logistica.adapter.out.persistence;

import br.com.devsombrero.logistica.adapter.out.persistence.cliente.ClienteDocument;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.support.ClienteFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteDocumentTest {

    @Test
    void fromDomainEToDomainDevemSerSimetricos() {
        Cliente cliente = ClienteFixture.clienteSalvo();

        ClienteDocument document = ClienteDocument.fromDomain(cliente);

        assertThat(document.id()).isEqualTo(cliente.id());
        assertThat(document.dataCriacao()).isEqualTo(cliente.dataCriacao());
        assertThat(ClienteDocument.toDomain(document)).isEqualTo(cliente);
    }

    @Test
    void atualizarDeveSubstituirApenasValoresPreenchidos() {
        ClienteDocument original = ClienteDocument.fromDomain(ClienteFixture.clienteSalvo());
        Cliente alteracao = new Cliente(
                "Maria Souza",
                null,
                "",
                "   ",
                "Rua B, 200",
                null,
                "RJ"
        );

        ClienteDocument atualizado = original.atualizar(alteracao);

        assertThat(atualizado.id()).isEqualTo(original.id());
        assertThat(atualizado.nome()).isEqualTo("Maria Souza");
        assertThat(atualizado.documento()).isEqualTo(original.documento());
        assertThat(atualizado.telefone()).isEqualTo(original.telefone());
        assertThat(atualizado.email()).isEqualTo(original.email());
        assertThat(atualizado.endereco()).isEqualTo("Rua B, 200");
        assertThat(atualizado.cidade()).isEqualTo(original.cidade());
        assertThat(atualizado.estado()).isEqualTo("RJ");
        assertThat(atualizado.dataCriacao()).isEqualTo(original.dataCriacao());
        assertThat(atualizado.dataAtualizacao()).isEqualTo(original.dataAtualizacao());
    }
}
