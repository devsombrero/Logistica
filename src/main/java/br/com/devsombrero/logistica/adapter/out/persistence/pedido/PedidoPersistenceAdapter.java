package br.com.devsombrero.logistica.adapter.out.persistence.pedido;

import br.com.devsombrero.logistica.application.port.out.pedido.PedidoPersistenceOutputPort;
import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import br.com.devsombrero.logistica.domain.exception.EntidadeNotFoundException;
import br.com.devsombrero.logistica.domain.model.Paginado;
import br.com.devsombrero.logistica.domain.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoPersistenceAdapter implements PedidoPersistenceOutputPort {
    private static final int TAMANHO_PAGINA_PADRAO = 20;
    private static final Sort ORDENACAO_PADRAO = Sort.by(Sort.Direction.DESC, "dataPedido");

    private final PedidoMongoRepository pedidoMongoRepository;

    @Override
    public Paginado<Pedido> buscarPedidoPorStatus(StatusPedido statusPedido, int tamanhoPagina, int paginaAtual) {
        Page<PedidoDocument> pagina = pedidoMongoRepository.findByStatusPedido(
                statusPedido,
                criarPageable(tamanhoPagina, paginaAtual)
        );
        return toPaginado(pagina);
    }

    @Override
    public Paginado<Pedido> buscarPedidosPorCliente(String clienteId, int tamanhoPagina, int paginaAtual) {
        Page<PedidoDocument> pagina = pedidoMongoRepository.findByClienteId(
                clienteId,
                criarPageable(tamanhoPagina, paginaAtual)
        );
        return toPaginado(pagina);
    }

    @Override
    public Paginado<Pedido> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        Page<PedidoDocument> pagina = pedidoMongoRepository.findAll(
                criarPageable(tamanhoPagina, paginaAtual)
        );
        return toPaginado(pagina);
    }

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return pedidoMongoRepository.findById(id)
                .map(PedidoDocument::toDomain);
    }

    @Override
    public Pedido salvar(Pedido entidade) {
        PedidoDocument salvo = pedidoMongoRepository.save(PedidoDocument.fromDomain(entidade));
        return PedidoDocument.toDomain(salvo);
    }

    @Override
    public Pedido atualizar(String id, Pedido entidade) {
        PedidoDocument atual = pedidoMongoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNotFoundException(
                        "Pedido " + id + " não encontrado"));

        PedidoDocument atualizado = pedidoMongoRepository.save(atual.atualizar(entidade));
        return PedidoDocument.toDomain(atualizado);
    }

    @Override
    public void deletar(String id) {
        if (!pedidoMongoRepository.existsById(id)) {
            throw new EntidadeNotFoundException("Pedido " + id + " não encontrado");
        }
        pedidoMongoRepository.deleteById(id);
    }

    private Pageable criarPageable(int tamanhoPagina, int paginaAtual) {
        int pagina = Math.max(paginaAtual, 0);
        int tamanho = tamanhoPagina > 0 ? tamanhoPagina : TAMANHO_PAGINA_PADRAO;
        return PageRequest.of(pagina, tamanho, ORDENACAO_PADRAO);
    }

    private Paginado<Pedido> toPaginado(Page<PedidoDocument> pagina) {
        return new Paginado<>(
                pagina.getContent().stream()
                        .map(PedidoDocument::toDomain)
                        .toList(),
                pagina.getNumber(),
                pagina.getSize(),
                (int) pagina.getTotalElements(),
                pagina.getTotalPages()
        );
    }
}