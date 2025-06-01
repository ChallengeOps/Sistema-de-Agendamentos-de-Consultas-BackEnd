package com.sistema_de_agendamentos.controller.dto.servico;

public record ServicoListagemDTO(
        Integer id,
        String nome,
        String descricao,
        Integer duracaoEmMinutos,
        String nomeProfissional
) {}