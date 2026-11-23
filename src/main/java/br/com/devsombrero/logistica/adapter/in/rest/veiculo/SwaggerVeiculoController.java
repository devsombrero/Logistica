package br.com.devsombrero.logistica.adapter.in.rest.veiculo;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "VeiculoController", description = "Endpoints de controle de Veiculos")
public interface SwaggerVeiculoController {

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
}
