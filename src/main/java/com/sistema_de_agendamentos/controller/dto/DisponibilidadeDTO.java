package com.sistema_de_agendamentos.controller.dto;

import java.time.LocalDateTime;

public record DisponibilidadeDTO(

        String diaDeSemana,
        LocalDateTime horaInicio,
        LocalDateTime horaFim
) {}