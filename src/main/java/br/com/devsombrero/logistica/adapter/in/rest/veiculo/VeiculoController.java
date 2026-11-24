package br.com.devsombrero.logistica.adapter.in.rest.veiculo;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoVeiculo;
import br.com.devsombrero.logistica.domain.dto.RetornoVeiculo;
import br.com.devsombrero.logistica.domain.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/veiculos")
@RequiredArgsConstructor
public class VeiculoController implements SwaggerVeiculoController {

    private final VeiculoService veiculoService;

    @Override
    public RetornoVeiculo cadastrar(@Valid @RequestBody RequisicaoVeiculo requisicaoVeiculo) {
        return this.veiculoService.cadastrar(requisicaoVeiculo.toDomain());
    }

    @Override
    public RetornoVeiculo buscarPorId(@PathVariable String id) {
        return this.veiculoService.buscarPorId(id);
    }

    @Override
    public RetornoVeiculo buscarPorPlaca(@PathVariable String placa) {
        return this.veiculoService.buscarPorPlaca(placa);
    }

    @Override
    public RetornoVeiculo atualizar(
            @PathVariable String id,
            @RequestBody RequisicaoVeiculo requisicaoVeiculo
    ) {
        return this.veiculoService.atualizar(id, requisicaoVeiculo.toDomain());
    }

    @Override
    public void apagar(@PathVariable String id) {
        this.veiculoService.apagar(id);
    }

}
