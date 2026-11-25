package br.com.devsombrero.logistica.adapter.out.persistence.pedido;

import br.com.devsombrero.logistica.domain.enumerator.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoMongoRepository extends MongoRepository<PedidoDocument, String> {
    Page<PedidoDocument> findByClienteId(String clienteId, Pageable pageable);
    Page<PedidoDocument> findByStatusPedido(StatusPedido statusPedido, Pageable pageable);
}
