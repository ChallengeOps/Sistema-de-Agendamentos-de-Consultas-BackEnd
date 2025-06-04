package com.sistema_de_agendamentos.mapper;

import com.sistema_de_agendamentos.config.security.dto.RegisterRequestDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioRegisterDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario fromRegisterRequestDTO(RegisterRequestDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setPassword(dto.password());
        // Nome vem com outro nome de campo: "name"
        usuario.setNome(dto.name());

        // Campos ignorados:
        usuario.setId(null);
        usuario.setAgendamentos(null);
        usuario.setServicos(null);
        usuario.setDisponibilidades(null);
        usuario.setAcesso(null);

        return usuario;
    }

    public Usuario updateFromDTO(UsuarioDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) return usuario;

        // Só atualiza se os campos não forem nulos
        if (dto.nome() != null) usuario.setNome(dto.nome());
        if (dto.email() != null) usuario.setEmail(dto.email());
        if (dto.acesso() != null) usuario.setAcesso(dto.acesso());

        return usuario;
    }

    public Usuario fromRegisterDTO(UsuarioRegisterDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setPassword(dto.senha());
        usuario.setAcesso(dto.acesso());

        // Campos ignorados:
        usuario.setId(null);
        usuario.setAgendamentos(null);
        usuario.setServicos(null);
        usuario.setDisponibilidades(null);

        return usuario;
    }
}
