package br.com.devsombrero.logistica.adapter.in.rest;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoMotorista;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.service.MotoristaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/motoristas")
@RequiredArgsConstructor
public class MotoristaController implements SwaggerMotoristaController {

    private final MotoristaService motoristaService;

    @Override
    public RetornoMotorista cadastrar(@Valid @RequestBody RequisicaoMotorista requisicaoMotorista) {
        return this.motoristaService.cadastrar(requisicaoMotorista.toDomain());
    }
}
