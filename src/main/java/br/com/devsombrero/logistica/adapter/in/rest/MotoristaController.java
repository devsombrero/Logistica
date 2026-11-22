package br.com.devsombrero.logistica.adapter.in.rest;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoMotorista;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.service.MotoristaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/motoristas")
@RequiredArgsConstructor
public class MotoristaController implements SwaggerMotoristaController {

    private final MotoristaService motoristaService;

    @Override
    public RetornoMotorista cadastrar(@Valid @RequestBody RequisicaoMotorista requisicaoMotorista) {
        return this.motoristaService.cadastrar(requisicaoMotorista.toDomain());
    }

    @Override
    public RetornoMotorista buscarPorId(@PathVariable String id) {
        return this.motoristaService.buscarPorId(id);
    }

    @Override
    public RetornoMotorista buscarPorCnh(@RequestParam String cnh) {
        return this.motoristaService.buscarPorCnh(cnh);
    }

    @Override
    public RetornoMotorista buscarPorCpf(@RequestParam String cpf) {
        return this.motoristaService.buscarPorCpf(cpf);
    }

    @Override
    public RetornoMotorista atualizar(
            @PathVariable String id,
            @RequestBody RequisicaoMotorista requisicaoMotorista
    ) {
        return this.motoristaService.atualizar(id, requisicaoMotorista.toDomain());
    }
}
