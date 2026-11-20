package br.com.devsombrero.logistica.adapter.out.persistence.produto;

import br.com.devsombrero.logistica.domain.model.Produto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static br.com.devsombrero.logistica.utils.PersistenceUtils.valorOuAtual;

@Document("produtos")
public record ProdutoDocument(
        @Id
        String id,
        String sku,
        String nome,
        String descricao,
        BigDecimal peso,
        BigDecimal altura,
        BigDecimal comprimento,
        BigDecimal valor,
        @CreatedDate
        LocalDateTime dataCriacao,
        @LastModifiedDate
        LocalDateTime dataAtualizacao
) {
    public static ProdutoDocument fromDomain(Produto produto) {
        return new ProdutoDocument(
                produto.id(),
                produto.sku(),
                produto.nome(),
                produto.descricao(),
                produto.peso(),
                produto.altura(),
                produto.comprimento(),
                produto.valor(),
                produto.dataCriacao(),
                produto.dataAtualizacao()
        );
    }

    public static Produto toDomain(ProdutoDocument produtoDocument) {
        return new Produto(
                produtoDocument.id(),
                produtoDocument.sku(),
                produtoDocument.nome(),
                produtoDocument.descricao(),
                produtoDocument.peso(),
                produtoDocument.altura(),
                produtoDocument.comprimento(),
                produtoDocument.valor(),
                produtoDocument.dataCriacao(),
                produtoDocument.dataAtualizacao()
        );
    }

    public ProdutoDocument atualizar(Produto produto) {
        return new ProdutoDocument(
                this.id,
                valorOuAtual(this.sku, produto.sku()),
                valorOuAtual(this.nome, produto.nome()),
                valorOuAtual(this.descricao, produto.descricao()),
                valorOuAtual(this.peso, produto.peso()),
                valorOuAtual(this.altura, produto.altura()),
                valorOuAtual(this.comprimento, produto.comprimento()),
                valorOuAtual(this.valor, produto.valor()),
                this.dataCriacao,
                this.dataAtualizacao
        );

    }

}
