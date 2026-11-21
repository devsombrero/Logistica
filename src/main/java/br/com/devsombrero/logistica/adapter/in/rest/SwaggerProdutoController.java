package br.com.devsombrero.logistica.adapter.in.rest;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoProduto;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.model.Paginado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ProdutoController", description = "Endpoints de controle de Produtos")
public interface SwaggerProdutoController {

    @Operation(
            summary = "Buscar produtos paginados",
            description = "Retorna produtos de forma paginada."
    )
    @ApiResponse(responseCode = "200", description = "Produtos retornados com sucesso")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping
    Paginado<RetornoProduto> buscarPaginado(
            @Parameter(description = "Quantidade de registros por página")
            @RequestParam(defaultValue = "10") int tamanhoPagina,

            @Parameter(description = "Número da página, começando em zero")
            @RequestParam(defaultValue = "0") int paginaAtual
    );

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

    @Operation(
            summary = "Buscar produto por ID",
            description = "Busca um produto através do seu identificador."
    )
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/{id}")
    RetornoProduto buscarPorId(@Parameter String id);

    @Operation(
            summary = "Buscar produto por SKU",
            description = "Busca um produto através doo sku."
    )
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/sku/{sku}")
    RetornoProduto buscarPorSku(@Parameter String sku);

    @Operation(
            summary = "Atualizar produto",
            description = "Atualizar cadastro de produto."
    )
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "422", description = "Produto já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RetornoProduto atualizar(
            @Parameter String id,
            @RequestBody(description = "Dados para atualização do produto")
            RequisicaoProduto requisicaoProduto
    );

    @Operation(
            summary = "Excluir produto",
            description = "Exclui um produto através do seu identificador."
    )
    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void apagar(
            @Parameter(description = "ID do produto", required = true)
            String id
    );
}
