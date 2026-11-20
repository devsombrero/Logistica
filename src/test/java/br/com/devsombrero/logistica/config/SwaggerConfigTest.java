package br.com.devsombrero.logistica.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigTest {

    @Test
    void deveConfigurarInformacoesDaApi() {
        OpenAPI openAPI = new SwaggerConfig().customOpenAPI();

        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Logística API");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
        assertThat(openAPI.getInfo().getDescription()).contains("Sistema de Gerenciamento Logístico");
        assertThat(openAPI.getInfo().getContact().getName()).isEqualTo("Dev Sombrero");
        assertThat(openAPI.getInfo().getLicense().getName()).isEqualTo("Apache 2.0");
    }
}
