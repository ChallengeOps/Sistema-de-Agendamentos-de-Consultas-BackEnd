package com.sistema_de_agendamentos.controller.dto.disponibilidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DisponibilidadeDTO(
        @NotBlank(message = "Dia da semana é obrigatório")
        String diaDeSemana,

        @NotNull(message = "Hora de início é obrigatória")
        LocalDateTime horaInicio,

        @NotNull(message = "Hora de fim é obrigatória")
        LocalDateTime horaFim
) {}