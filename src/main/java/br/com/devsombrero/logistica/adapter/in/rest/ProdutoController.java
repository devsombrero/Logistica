package br.com.devsombrero.logistica.adapter.in.rest;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoProduto;
import br.com.devsombrero.logistica.domain.dto.RetornoProduto;
import br.com.devsombrero.logistica.domain.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController implements SwaggerProdutoController {

    private final ProdutoService produtoService;

    @Override
    public RetornoProduto cadastrar(
            @Valid @RequestBody RequisicaoProduto requisicaoProduto
    ) {
        return this.produtoService.cadastrar(requisicaoProduto.toDomain());
    }

    @Override
    public RetornoProduto buscarPorId(@PathVariable String id) {
        return this.produtoService.buscarPorId(id);
    }

    @Override
    public RetornoProduto buscarPorSku(@PathVariable String sku) {
        return this.produtoService.buscarPorSku(sku);
    }

    @Override
    public RetornoProduto atualizar(
            @PathVariable String id,
            @RequestBody RequisicaoProduto requisicaoProduto
    ) {
        return produtoService.atualizar(id, requisicaoProduto.toDomain());
    }

    @Override
    public void apagar(@PathVariable String id) {
        produtoService.apagar(id);
    }
}
