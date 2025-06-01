package com.sistema_de_agendamentos.controller.dto;

import java.util.List;

public record ServicoDetailsDTO(
        Integer id,
        String nome,
        String descricao,
        Integer duracaoEmMinutos,
        String profissional,
        List<DisponibilidadeDTO> disponibilidades
) {}