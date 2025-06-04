package com.sistema_de_agendamentos.controller.dto.disponibilidade;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
public record DisponibilidadeDTO(

        @NotBlank(message = "Data é obrigatória no formato yyyy-MM-dd")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Data deve estar no formato yyyy-MM-dd")
        String data,

        @NotBlank(message = "Horário de início é obrigatório no formato HH:mm")
        @Pattern(regexp = "\\d{2}:\\d{2}", message = "Horário de início deve estar no formato HH:mm")
        String horarioInicio,

        @NotBlank(message = "Horário de fim é obrigatório no formato HH:mm")
        @Pattern(regexp = "\\d{2}:\\d{2}", message = "Horário de fim deve estar no formato HH:mm")
        String horarioFim
) {}
