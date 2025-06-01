package com.sistema_de_agendamentos.controller.dto.servico;

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