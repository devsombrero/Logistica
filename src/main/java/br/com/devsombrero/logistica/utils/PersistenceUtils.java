package br.com.devsombrero.logistica.utils;

import java.math.BigDecimal;

public class PersistenceUtils {
    public static String valorOuAtual(String valorAtual, String novoValor) {
        return novoValor == null || novoValor.isBlank()
                ? valorAtual
                : novoValor;
    }

    public static BigDecimal valorOuAtual(BigDecimal valorAtual, BigDecimal novoValor) {
        return novoValor == null ? valorAtual : novoValor;
    }
    public static <T> T valorOuAtual(T valorAtual, T novoValor) {
        return novoValor == null ? valorAtual : novoValor;
    }

}
