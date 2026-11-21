package br.com.devsombrero.logistica.adapter.in.rest;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoMotorista;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "MotoristaController", description = "Endpoints de controle de Motoristas")
public interface SwaggerMotoristaController {

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

}
