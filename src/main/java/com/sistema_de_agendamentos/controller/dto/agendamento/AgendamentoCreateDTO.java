package com.sistema_de_agendamentos.controller.dto.agendamento;

import jakarta.validation.constraints.NotNull;

public record AgendamentoCreateDTO(
        @NotNull(message = "O serviço é obrigatório.")
        Integer servicoId,

        @NotNull(message = "A disponibilidade é obrigatória.")
        Integer disponibilidadeId
) {}