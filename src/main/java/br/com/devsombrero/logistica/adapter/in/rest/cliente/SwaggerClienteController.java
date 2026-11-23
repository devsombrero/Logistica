package br.com.devsombrero.logistica.adapter.in.rest.cliente;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoCliente;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ClienteController", description = "Endpoints de controle de Clientes")
public interface SwaggerClienteController {

    @Operation(
            summary = "Buscar clientes paginados",
            description = "Retorna clientes de forma paginada."
    )
    @ApiResponse(responseCode = "200", description = "Clientes retornados com sucesso")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping
    Paginado<RetornoCliente> buscarPaginado(
            @Parameter(description = "Quantidade de registros por página")
            @RequestParam(defaultValue = "10") int tamanhoPagina,

            @Parameter(description = "Número da página, começando em zero")
            @RequestParam(defaultValue = "0") int paginaAtual
    );

    @Operation(
            summary = "Cadastrar cliente",
            description = "Realiza o cadastro de um novo cliente."
    )
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "422", description = "Cliente já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    RetornoCliente cadastrar(
            @RequestBody(description = "Dados para cadastro do cliente")
            RequisicaoCliente requisicaoCliente
    );

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Busca um cliente através do seu identificador."
    )
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/{id}")
    RetornoCliente buscarPorId(@Parameter String id);

    @Operation(
            summary = "Buscar cliente por e-mail",
            description = "Busca um cliente através do seu endereço de e-mail."
    )
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/email")
    RetornoCliente buscarPorEmail(@Parameter String email);

    @Operation(
            summary = "Buscar cliente por documento",
            description = "Busca um cliente através do CPF ou CNPJ."
    )
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/documento")
    RetornoCliente buscarPorDocumento(@Parameter String documento);

    @Operation(
            summary = "Atualizar cliente",
            description = "Atualizar cadastro de cliente."
    )
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "422", description = "Cliente já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RetornoCliente atualizar(
            @Parameter String id,
            @RequestBody(description = "Dados para cadastro do cliente")
            RequisicaoCliente requisicaoCliente
    );

    @Operation(
            summary = "Excluir cliente",
            description = "Exclui um cliente através do seu identificador."
    )
    @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void apagar(
            @Parameter(description = "ID do cliente", required = true)
            String id
    );
}