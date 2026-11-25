package br.com.devsombrero.logistica.adapter.in.rest.pedido;

import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoAtualizacaoPedido;
import br.com.devsombrero.logistica.adapter.in.dto.RequisicaoCadastroPedido;
import br.com.devsombrero.logistica.domain.dto.RetornoPedido;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController implements SwaggerPedidoController {

    private final PedidoService pedidoService;

    @Override
    public RetornoPedido cadastrar(RequisicaoCadastroPedido request) {
        return this.pedidoService.cadastrar(request.toDomain());
    }

    @Override
    public RetornoPedido buscarPorId(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Paginado<RetornoPedido>> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        return null;
    }

    @Override
    public ResponseEntity<Paginado<RetornoPedido>> buscarPorCliente(String clienteId, int tamanhoPagina, int paginaAtual) {
        return null;
    }

    @Override
    public ResponseEntity<Paginado<RetornoPedido>> buscarPorStatus(StatusPedido status, int tamanhoPagina, int paginaAtual) {
        return null;
    }

    @Override
    public ResponseEntity<RetornoPedido> atualizar(String id, RequisicaoAtualizacaoPedido request) {
        return null;
    }

    @Override
    public ResponseEntity<RetornoPedido> cancelar(String id) {
        return null;
    }
}
