package com.sistema_de_agendamentos.controller.dto;

import com.sistema_de_agendamentos.entity.Usuario.ClienteTipo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotNull(message = "Tipo de acesso é obrigatório")
        ClienteTipo acesso
) {}