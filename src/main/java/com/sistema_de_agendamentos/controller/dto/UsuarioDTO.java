package com.sistema_de_agendamentos.controller.dto;

import com.sistema_de_agendamentos.entity.Usuario.ClienteTipo;

public record UsuarioDTO(
        Integer id,
        String nome,
        String email,
        ClienteTipo acesso
) {}