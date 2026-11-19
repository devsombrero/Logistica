package br.com.devsombrero.logistica.adapter.in;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoCliente;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/clientes")
@RequiredArgsConstructor
public class ClienteController implements SwaggerClienteController {

    private final ClienteService clienteService;

    @Override
    public RetornoCliente cadastrar(
            @RequestBody RequisicaoCliente requisicaoCliente) {
        return clienteService.cadastrar(requisicaoCliente.toDomain());
    }
}
