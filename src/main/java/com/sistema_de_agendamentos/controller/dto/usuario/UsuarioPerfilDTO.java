package com.sistema_de_agendamentos.controller.dto.usuario;

import com.sistema_de_agendamentos.entity.Usuario;

public record UsuarioPerfilDTO(
        Integer id,
        String nome,
        String email,
        Usuario.ClienteTipo acesso
) {}
