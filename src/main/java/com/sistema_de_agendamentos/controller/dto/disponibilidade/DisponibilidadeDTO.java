package com.sistema_de_agendamentos.controller.dto.disponibilidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DisponibilidadeDTO(
        @NotBlank(message = "Dia da semana é obrigatório")
        String diaDeSemana,

        @NotNull(message = "Hora de início é obrigatória")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime horaInicio,

        @NotNull(message = "Hora de fim é obrigatória")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime horaFim
) {}