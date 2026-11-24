package br.com.devsombrero.logistica.adapter.in.rest.veiculo;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoVeiculo;
import br.com.devsombrero.logistica.domain.dto.FiltroPesquisaVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.model.Paginado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VeiculoController", description = "Endpoints de controle de Veiculos")
public interface SwaggerVeiculoController {

    @Operation(
            summary = "Buscar veículos paginados",
            description = "Realiza a busca paginada de veículos com filtros opcionais."
    )
    @ApiResponse(responseCode = "200", description = "Veículos encontrados")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping
    Paginado<RetornoVeiculo> buscarPaginado(
            @Parameter(description = "Número da página (inicia em 0)")
            @RequestParam(defaultValue = "0") int paginaAtual,

            @Parameter(description = "Quantidade de registros por página")
            @RequestParam(defaultValue = "10") int tamanhoPagina,

            @Parameter(description = "Filtros de pesquisa")
            @ModelAttribute FiltroPesquisaVeiculo filtro
    );

    @Operation(
            summary = "Cadastrar veiculo",
            description = "Realiza o cadastro de um novo veiculo."
    )
    @ApiResponse(responseCode = "201", description = "Veiculo cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "422", description = "Veiculo já cadastrado com placa existente")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    RetornoVeiculo cadastrar(
            @RequestBody(description = "Dados para cadastro do veiculo")
            RequisicaoVeiculo requisicaoVeiculo
    );

    @Operation(
            summary = "Buscar veiculo por ID",
            description = "Busca um veiculo através do seu identificador."
    )
    @ApiResponse(responseCode = "200", description = "Veiculo encontrado")
    @ApiResponse(responseCode = "404", description = "Veiculo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/{id}")
    RetornoVeiculo buscarPorId(@Parameter String id);

    @Operation(
            summary = "Buscar veiculo por placa",
            description = "Busca um veiculo através do registro da placa."
    )
    @ApiResponse(responseCode = "200", description = "Veiculo encontrado")
    @ApiResponse(responseCode = "404", description = "Veiculo não encontrado com essa placa")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/placa/{placa}")
    RetornoVeiculo buscarPorPlaca(@Parameter String placa);

    @Operation(
            summary = "Atualizar veiculo",
            description = "Atualizar cadastro de veiculo."
    )
    @ApiResponse(responseCode = "200", description = "Veiculo atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "422", description = "Veiculo já cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RetornoVeiculo atualizar(
            @Parameter String id,
            @RequestBody(description = "Dados para atualização do veiculo")
            RequisicaoVeiculo requisicaoVeiculo
    );

    @Operation(
            summary = "Apagar veiculo por ID",
            description = "Apagar um veiculo através do seu identificador."
    )
    @ApiResponse(responseCode = "204", description = "Veiculo excluido com sucesso")
    @ApiResponse(responseCode = "404", description = "Veiculo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void apagar(@Parameter String id);
}
