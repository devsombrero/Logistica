package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.cliente.*;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Cliente;
import br.com.devsombrero.logistica.domain.model.Paginado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService  {

    private final CadastrarClienteInputPort cadastrarClienteInputPort;
    private final BuscarClienteInputPort buscarClienteInputPort;
    private final AtualizarClienteInputPort atualizarClienteInputPort;
    private final ApagarClienteInputPort apagarClienteInputPort;
    private final BuscarClientePaginadoInputPort buscarPaginadoInputPort;

    @Override
    public Paginado<RetornoCliente> buscarPaginado(int tamanhoPagina, int paginaAtual) {
        return buscarPaginadoInputPort.buscarPaginado(
                tamanhoPagina,
                paginaAtual
        );
    }

    @Override
    public RetornoCliente cadastrar(Cliente cliente) {
        return cadastrarClienteInputPort.cadastrar(cliente);
    }

    @Override
    public RetornoCliente buscarPorId(String id) {
        return buscarClienteInputPort.buscarPorId(id);
    }

    @Override
    public RetornoCliente buscarPorEmail(String email) {
        return buscarClienteInputPort.buscarPorEmail(email);
    }

    @Override
    public RetornoCliente buscarPorDocumento(String documento) {
        return buscarClienteInputPort.buscarPorDocumento(documento);
    }

    @Override
    public RetornoCliente atualizar(String id, Cliente cliente) {
        return atualizarClienteInputPort.atualizar(id, cliente);
    }

    @Override
    public void apagar(String id) {
        apagarClienteInputPort.apagar(id);
    }
}
