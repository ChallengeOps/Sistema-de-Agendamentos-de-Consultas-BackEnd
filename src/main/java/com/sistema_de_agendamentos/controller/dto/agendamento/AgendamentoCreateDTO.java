package com.sistema_de_agendamentos.controller.dto.agendamento;

public record AgendamentoCreateDTO(

        Integer profissionalId,
        Integer servicoId,
        Integer disponibilidadeId
) {}