package br.com.devsombrero.logistica.domain.service;

import br.com.devsombrero.logistica.application.port.in.motorista.*;
import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;
import br.com.devsombrero.logistica.domain.model.Paginado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MotoristaServiceImpl implements MotoristaService {

    private final CadastrarMotoristaInputPort cadastrarMotoristaInputPort;
    private final BuscarMotoristaInputPort buscarMotoristaInputPort;
    private final AtualizarMotoristaInputPort atualizarMotoristaInputPort;
    private final BuscarMotoristaPaginadoInputPort buscarMotoristaPaginadoInputPort;
    private final ApagarMotoristaInputPort apagarMotoristaInputPort;

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

    @Override
    public RetornoMotorista atualizar(String id, Motorista motorista) {
        return this.atualizarMotoristaInputPort.atualizar(id, motorista);
    }

    @Override
    public Paginado<RetornoMotorista> buscarPaginado(int paginaAtual, int tamanhoPagina) {
        return this.buscarMotoristaPaginadoInputPort.buscarPaginado(paginaAtual, tamanhoPagina);
    }

    @Override
    public void apagar(String id) {
        this.apagarMotoristaInputPort.apagar(id);
    }
}
