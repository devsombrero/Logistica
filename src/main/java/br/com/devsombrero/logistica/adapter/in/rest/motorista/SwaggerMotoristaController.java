package br.com.devsombrero.logistica.adapter.in.rest.motorista;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoMotorista;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Paginado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MotoristaController", description = "Endpoints de controle de Motoristas")
public interface SwaggerMotoristaController {

    @Operation(
            summary = "Buscar motorista paginados",
            description = "Retorna motoristas de forma paginada."
    )
    @ApiResponse(responseCode = "200", description = "Motorista retornados com sucesso")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping
    Paginado<RetornoMotorista> buscarPaginado(
            @Parameter(description = "Quantidade de registros por página")
            @RequestParam(defaultValue = "10") int tamanhoPagina,

            @Parameter(description = "Número da página, começando em zero")
            @RequestParam(defaultValue = "0") int paginaAtual
    );

    @Operation(
            summary = "Cadastrar motorista",
            description = "Realiza o cadastro de um novo motorista."
    )
    @ApiResponse(responseCode = "201", description = "Motorista cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "422", description = "Motorista já cadastrado com CPF/CNH existentes")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    RetornoMotorista cadastrar(
            @RequestBody(description = "Dados para cadastro do motorista")
            RequisicaoMotorista requisicaoMotorista
    );

    @Operation(
            summary = "Buscar motorista por ID",
            description = "Busca um motorista através do seu identificador."
    )
    @ApiResponse(responseCode = "200", description = "Motorista encontrado")
    @ApiResponse(responseCode = "404", description = "Motorista não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/{id}")
    RetornoMotorista buscarPorId(@Parameter String id);

    @Operation(
            summary = "Buscar motorista por cnh",
            description = "Busca um motorista através do seu endereço de cnh."
    )
    @ApiResponse(responseCode = "200", description = "Motorista encontrado")
    @ApiResponse(responseCode = "404", description = "Motorista não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/cnh")
    RetornoMotorista buscarPorCnh(@Parameter String cnh);

    @Operation(
            summary = "Buscar motorista por CPF",
            description = "Busca um motorista através do CPF"
    )
    @ApiResponse(responseCode = "200", description = "Motorista encontrado")
    @ApiResponse(responseCode = "404", description = "Motorista não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/cpf")
    RetornoMotorista buscarPorCpf(@Parameter String cpf);

    @Operation(
            summary = "Atualizar motorista",
            description = "Atualizar cadastro de motorista."
    )
    @ApiResponse(responseCode = "200", description = "Motorista atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "422", description = "Motorista já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RetornoMotorista atualizar(
            @Parameter String id,
            @RequestBody(description = "Dados para atualização do motorista")
            RequisicaoMotorista requisicaoMotorista
    );

    @Operation(
            summary = "Excluir motorista",
            description = "Exclui um motorista através do seu identificador."
    )
    @ApiResponse(responseCode = "204", description = "Motorista excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Motorista não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void apagar(
            @Parameter(description = "ID do motorista", required = true)
            String id
    );
}
