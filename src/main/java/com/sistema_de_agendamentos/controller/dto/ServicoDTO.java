package com.sistema_de_agendamentos.controller.dto;

public record ServicoDTO(
        String nome,
        String descricao,
        Integer duracaoEmMinutos
) {}