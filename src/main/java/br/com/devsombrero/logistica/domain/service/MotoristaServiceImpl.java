package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.motorista.CadastrarMotoristaInputPort;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MotoristaServiceImpl implements MotoristaService {

    private final CadastrarMotoristaInputPort cadastrarMotoristaInputPort;

    @Override
    public RetornoMotorista cadastrar(Motorista motorista) {
        return this.cadastrarMotoristaInputPort.cadastrar(motorista);
    }
}
