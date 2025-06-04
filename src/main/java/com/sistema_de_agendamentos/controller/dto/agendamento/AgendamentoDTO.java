package com.sistema_de_agendamentos.controller.dto.agendamento;

public record AgendamentoDTO(
        Integer id,
        String nomeProfissional,
        String nomeServico,
        String dateDisponibilidade
) {
}
