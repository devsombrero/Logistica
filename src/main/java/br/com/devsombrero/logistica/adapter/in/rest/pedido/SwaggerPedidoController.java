package br.com.devsombrero.logistica.adapter.in.rest.pedido;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoAtualizacaoPedido;
import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoCadastroPedido;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.Paginado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pedidos", description = "Cadastro, consulta, edição e cancelamento de pedidos")
public interface SwaggerPedidoController {

    @Operation(
            summary = "Cadastrar pedido",
            description = "Cria um pedido com status PENDENTE. Nome e valor dos produtos são obtidos "
                    + "do cadastro no momento da compra, e o valor total é calculado a partir dos itens."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    RetornoPedido cadastrar(@RequestBody @Valid RequisicaoCadastroPedido request);

    @Operation(summary = "Buscar pedido por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RetornoPedido buscarPorId(
            @Parameter(description = "ID do pedido", example = "665f1d3f8a4b2c1d3e4f5a99")
            @PathVariable String id
    );

    @Operation(summary = "Listar pedidos", description = "Lista paginada, ordenada pela data do pedido (mais recentes primeiro).")
    @ApiResponse(responseCode = "200", description = "Página de pedidos retornada")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Paginado<RetornoPedido> buscarPaginado(
            @Parameter(description = "Quantidade de registros por página", example = "20")
            @RequestParam(defaultValue = "20") int tamanhoPagina,
            @Parameter(description = "Número da página, iniciando em 0", example = "0")
            @RequestParam(defaultValue = "0") int paginaAtual
    );

    @Operation(summary = "Listar pedidos de um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página de pedidos do cliente retornada"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/cliente/{clienteId}")
    @ResponseStatus(HttpStatus.OK)
    Paginado<RetornoPedido> buscarPorCliente(
            @Parameter(description = "ID do cliente", example = "665f1b9a8a4b2c1d3e4f5a01")
            @PathVariable String clienteId,
            @Parameter(description = "Quantidade de registros por página", example = "20")
            @RequestParam(defaultValue = "20") int tamanhoPagina,
            @Parameter(description = "Número da página, iniciando em 0", example = "0")
            @RequestParam(defaultValue = "0") int paginaAtual
    );

    @Operation(summary = "Listar pedidos por status")
    @ApiResponse(responseCode = "200", description = "Página de pedidos com o status informado")
    @GetMapping("/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    Paginado<RetornoPedido> buscarPorStatus(
            @Parameter(description = "Status do pedido", example = "PENDENTE")
            @PathVariable StatusPedido status,
            @Parameter(description = "Quantidade de registros por página", example = "20")
            @RequestParam(defaultValue = "20") int tamanhoPagina,
            @Parameter(description = "Número da página, iniciando em 0", example = "0")
            @RequestParam(defaultValue = "0") int paginaAtual
    );

    @Operation(
            summary = "Editar pedido",
            description = "Atualiza observação e/ou itens. Se os itens forem alterados, o valor total é recalculado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido ou produto não encontrado"),
            @ApiResponse(responseCode = "422", description = "Pedido não pode ser editado no status atual")
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RetornoPedido atualizar(
            @Parameter(description = "ID do pedido", example = "665f1d3f8a4b2c1d3e4f5a99")
            @PathVariable String id,
            @RequestBody @Valid RequisicaoAtualizacaoPedido request
    );

    @Operation(
            summary = "Cancelar pedido",
            description = "Altera o status para CANCELADO. Permitido apenas para pedidos PENDENTE ou APROVADO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "422", description = "Pedido não pode ser cancelado no status atual")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/cancelar")
    RetornoPedido cancelar(
            @Parameter(description = "ID do pedido", example = "665f1d3f8a4b2c1d3e4f5a99")
            @PathVariable String id
    );
}