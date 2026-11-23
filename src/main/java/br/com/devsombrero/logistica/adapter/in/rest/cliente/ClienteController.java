package br.com.devsombrero.logistica.adapter.in.rest.cliente;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoCliente;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/clientes")
@RequiredArgsConstructor
public class ClienteController implements SwaggerClienteController {

    private final ClienteService clienteService;

    @Override
    public Paginado<RetornoCliente> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        return clienteService.buscarPaginado(tamanhoPagina, paginaAtual);
    }

    @Override
    public RetornoCliente cadastrar(
            @Valid @RequestBody RequisicaoCliente requisicaoCliente) {
        return clienteService.cadastrar(requisicaoCliente.toDomain());
    }

    @Override
    public RetornoCliente buscarPorId(@PathVariable String id) {
        return clienteService.buscarPorId(id);
    }

    @Override
    public RetornoCliente buscarPorEmail(@RequestParam String email) {
        return clienteService.buscarPorEmail(email);
    }

    @Override
    public RetornoCliente buscarPorDocumento(@RequestParam String documento) {
        return clienteService.buscarPorDocumento(documento);
    }

    @Override
    public RetornoCliente atualizar(
            @PathVariable String id,
            @RequestBody RequisicaoCliente requisicaoCliente
    ) {
        return clienteService.atualizar(id, requisicaoCliente.toDomain());
    }

    @Override
    public void apagar(@PathVariable String id) {
        this.clienteService.apagar(id);
    }
}
