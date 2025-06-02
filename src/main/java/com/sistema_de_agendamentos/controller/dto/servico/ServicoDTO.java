package com.sistema_de_agendamentos.controller.dto.servico;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServicoDTO(
        @NotBlank(message = "Nome do serviço é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Duração é obrigatória")
        @Min(value = 1, message = "Duração deve ser maior que zero")
        Integer duracaoEmMinutos
) {}