package br.com.devsombrero.logistica.adapter.out.persistence.cliente;

import br.com.devsombrero.logistica.domain.model.Cliente;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import static br.com.devsombrero.logistica.utils.PersistenceUtils.valorOuAtual;

@Document("clientes")
public record ClienteDocument(
        @Id
        String id,
        String nome,
        String documento,
        String telefone,
        String email,
        String endereco,
        String cidade,
        String estado,
        @CreatedDate
        LocalDateTime dataCriacao,
        @LastModifiedDate
        LocalDateTime dataAtualizacao
) {

    static ClienteDocument fromDomain(Cliente cliente) {
        return new ClienteDocument(
                cliente.id(),
                cliente.nome(),
                cliente.documento(),
                cliente.telefone(),
                cliente.email(),
                cliente.endereco(),
                cliente.cidade(),
                cliente.estado(),
                cliente.dataCriacao(),
                cliente.dataAtualizacao()
        );
    }

    static Cliente toDomain(ClienteDocument clienteDocument) {
        return new Cliente(
                clienteDocument.id(),
                clienteDocument.nome(),
                clienteDocument.documento(),
                clienteDocument.telefone(),
                clienteDocument.email(),
                clienteDocument.endereco(),
                clienteDocument.cidade(),
                clienteDocument.estado(),
                clienteDocument.dataCriacao(),
                clienteDocument.dataAtualizacao()
        );
    }

    public ClienteDocument atualizar(Cliente cliente) {
        return new ClienteDocument(
                this.id,
                valorOuAtual(this.nome, cliente.nome()),
                valorOuAtual(this.documento, cliente.documento()),
                valorOuAtual(this.telefone, cliente.telefone()),
                valorOuAtual(this.email, cliente.email()),
                valorOuAtual(this.endereco, cliente.endereco()),
                valorOuAtual(this.cidade, cliente.cidade()),
                valorOuAtual(this.estado, cliente.estado()),
                this.dataCriacao,
                this.dataAtualizacao
        );
    }
}
