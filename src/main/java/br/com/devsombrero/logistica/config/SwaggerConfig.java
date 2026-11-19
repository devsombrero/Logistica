package br.com.devsombrero.logistica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Logística API")
                        .description("""
                                # Sistema de Gerenciamento Logístico

                                API REST desenvolvida para gerenciamento de operações logísticas,
                                permitindo controlar clientes, produtos, motoristas, veículos,
                                pedidos e entregas.

                                ## Módulos disponíveis

                                ### Clientes
                                - Cadastro de clientes.
                                - Consulta de clientes.
                                - Atualização de dados cadastrais.
                                - Exclusão de clientes.
                                - Consulta paginada.
                                - Validação de documentos e e-mails duplicados.

                                ### Produtos
                                - Cadastro de produtos.
                                - Consulta de produtos.
                                - Atualização de produtos.
                                - Exclusão de produtos.
                                - Gerenciamento de dimensões, peso e valores.

                                ### Motoristas
                                - Cadastro de motoristas.
                                - Consulta de motoristas.
                                - Atualização cadastral.
                                - Gerenciamento de CNH e validade.
                                - Controle de status operacional.

                                ### Veículos
                                - Cadastro de veículos.
                                - Consulta de veículos.
                                - Atualização de veículos.
                                - Controle de capacidade de carga.
                                - Gerenciamento de disponibilidade e manutenção.

                                ### Pedidos
                                - Cadastro de pedidos.
                                - Consulta de pedidos.
                                - Atualização de pedidos.
                                - Cancelamento de pedidos.
                                - Gerenciamento de itens e produtos.
                                - Cálculo automático do valor total.
                                - Controle do ciclo de vida do pedido.

                                ### Entregas
                                - Cadastro de entregas.
                                - Consulta de entregas.
                                - Associação de pedidos, motoristas e veículos.
                                - Atualização de status da entrega.
                                - Controle de saída e conclusão da entrega.

                                ## Regras de negócio

                                - Não permitir documentos duplicados para clientes.
                                - Não permitir CPF duplicado para motoristas.
                                - Não permitir placas duplicadas para veículos.
                                - Pedidos devem possuir cliente e pelo menos um produto.
                                - O valor total do pedido deve ser calculado pelos itens.
                                - Motoristas inativos não podem realizar entregas.
                                - Veículos em manutenção não podem ser utilizados.
                                - Veículos devem ter seu status atualizado durante as entregas.
                                - Produtos vinculados a pedidos não podem ser excluídos.
                                - Datas de criação e atualização são gerenciadas automaticamente.

                                ## Tecnologias utilizadas

                                - Java
                                - Spring Boot
                                - Spring Data MongoDB
                                - MongoDB
                                - Spring Validation
                                - SpringDoc OpenAPI
                                - Arquitetura Hexagonal
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Dev Sombrero")
                                .url("https://github.com/")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                );
    }
}