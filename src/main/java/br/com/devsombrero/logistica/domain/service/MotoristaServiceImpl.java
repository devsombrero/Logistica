package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.motorista.BuscarMotoristaInputPort;
import br.com.devsombrero.logistica.application.port.in.motorista.CadastrarMotoristaInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MotoristaServiceImpl implements MotoristaService {

    private final CadastrarMotoristaInputPort cadastrarMotoristaInputPort;
    private final BuscarMotoristaInputPort buscarMotoristaInputPort;

    @Override
    public RetornoMotorista cadastrar(Motorista motorista) {
        return this.cadastrarMotoristaInputPort.cadastrar(motorista);
    }

    @Override
    public RetornoMotorista buscarPorId(String id) {
        return this.buscarMotoristaInputPort.buscarPorId(id);
    }

    @Override
    public RetornoMotorista buscarPorCnh(String cnh) {
        return this.buscarMotoristaInputPort.buscarPorCnh(cnh);
    }

    @Override
    public RetornoMotorista buscarPorCpf(String cpf) {
        return this.buscarMotoristaInputPort.buscarPorCpf(cpf);
    }
}
