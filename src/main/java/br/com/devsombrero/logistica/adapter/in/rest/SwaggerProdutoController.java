package br.com.devsombrero.logistica.adapter.in.rest;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoProduto;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "ProdutoController", description = "Endpoints de controle de Produtos")
public interface SwaggerProdutoController {

    @Operation(
            summary = "Cadastrar produto",
            description = "Realiza o cadastro de um novo produto."
    )
    @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "422", description = "Produto já cadastrado com SKU existente")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    RetornoProduto cadastrar(
            @RequestBody(description = "Dados para cadastro do produto")
            RequisicaoProduto requisicaoProduto
    );
}
