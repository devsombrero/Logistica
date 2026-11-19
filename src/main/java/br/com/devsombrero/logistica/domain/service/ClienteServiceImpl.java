package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.CadastrarClienteInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoCliente;
import br.com.devsombrero.logistica.domain.model.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService  {

    private final CadastrarClienteInputPort cadastrarClienteInputPort;

    @Override
    public RetornoCliente cadastrar(Cliente cliente) {
        return cadastrarClienteInputPort.cadastrar(cliente);
    }
}
