package br.com.devsombrero.logistica.application.port.in.motorista;

import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Paginado;

public interface BuscarMotoristaPaginadoInputPort {
    Paginado<RetornoMotorista> buscarPaginado(int paginaAtual, int tamanhoPagina);
}
