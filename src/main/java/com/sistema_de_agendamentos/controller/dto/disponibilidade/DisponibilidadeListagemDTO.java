package com.sistema_de_agendamentos.controller.dto.disponibilidade;

import java.time.LocalDateTime;

public record DisponibilidadeListagemDTO(
        Integer id,
        String data,
        String horaInicio,
        String horaFim
) {}