package com.sistema_de_agendamentos.controller.dto;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;

import java.util.List;

public record ServicoDetailsDTO(
        Integer id,
        String nome,
        String descricao,
        Integer duracaoEmMinutos,
        String profissional,
        List<DisponibilidadeListagemDTO> disponibilidades
) {}