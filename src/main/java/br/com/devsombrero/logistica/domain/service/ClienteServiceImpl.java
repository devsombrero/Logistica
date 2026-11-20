package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.AtualizarClienteInputPort;
import br.com.devsombrero.logistica.application.port.in.BuscarClienteInputPort;
import br.com.devsombrero.logistica.application.port.in.CadastrarClienteInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService  {

    private final CadastrarClienteInputPort cadastrarClienteInputPort;
    private final BuscarClienteInputPort buscarClienteInputPort;
    private final AtualizarClienteInputPort atualizarClienteInputPort;

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
}
