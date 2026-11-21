package br.com.devsombrero.logistica.support;

import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ProdutoFixture {

    public static final String ID = "775f1c2e9b1e8a3d4c5b6a8e";
    public static final String SKU = "SKU-001";
    public static final LocalDateTime DATA = LocalDateTime.of(2026, 2, 15, 9, 45);

    private ProdutoFixture() {
    }

    public static Produto novoProduto() {
        return new Produto(
                SKU,
                "Caixa",
                "Caixa de papelão",
                new BigDecimal("1.50"),
                new BigDecimal("30.00"),
                new BigDecimal("40.00"),
                new BigDecimal("19.90")
        );
    }

    public static Produto produtoSalvo() {
        return new Produto(
                ID,
                SKU,
                "Caixa",
                "Caixa de papelão",
                new BigDecimal("1.50"),
                new BigDecimal("30.00"),
                new BigDecimal("40.00"),
                new BigDecimal("19.90"),
                DATA,
                DATA
        );
    }

    public static RetornoProduto retornoProduto() {
        return new RetornoProduto(
                ID,
                SKU,
                "Caixa",
                "Caixa de papelão",
                new BigDecimal("1.50"),
                new BigDecimal("30.00"),
                new BigDecimal("40.00"),
                new BigDecimal("19.90")
        );
    }

    public static String json(String sku, String nome, String descricao, String peso,
                              String altura, String comprimento, String valor) {
        return """
                {
                  "sku": %s,
                  "nome": %s,
                  "descricao": %s,
                  "peso": %s,
                  "altura": %s,
                  "comprimento": %s,
                  "valor": %s
                }
                """.formatted(q(sku), q(nome), q(descricao), n(peso), n(altura), n(comprimento), n(valor));
    }

    public static String jsonValido() {
        return json(SKU, "Caixa", "Caixa de papelão", "1.50", "30.00", "40.00", "19.90");
    }

    private static String q(String valor) {
        return valor == null ? "null" : "\"" + valor + "\"";
    }

    private static String n(String valor) {
        return valor == null ? "null" : valor;
    }
}
