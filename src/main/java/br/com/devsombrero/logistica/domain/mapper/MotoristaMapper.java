package br.com.devsombrero.logistica.domain.mapper;

import br.com.devsombrero.logistica.domain.dto.RetornoMotorista;
import br.com.devsombrero.logistica.domain.model.Motorista;

public final class MotoristaMapper {
    public static RetornoMotorista toResponse(Motorista motorista) {
        return new RetornoMotorista(
                motorista.id(),
                motorista.nome(),
                motorista.cpf(),
                motorista.telefone(),
                motorista.cnh(),
                motorista.validadeCnh(),
                motorista.status()
        );
    }
}
