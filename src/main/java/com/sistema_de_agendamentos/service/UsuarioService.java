package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.usuario.ProfissionaisDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioPerfilDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioRegisterDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.mapper.UsuarioMapper;
import com.sistema_de_agendamentos.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public Usuario create(UsuarioRegisterDTO usuarioDTO) {
        var usuario = usuarioMapper.fromRegisterDTO(usuarioDTO);
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    public UsuarioPerfilDTO me(){
        Usuario usuario = getAuthenticationUser();
        return new UsuarioPerfilDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getAcesso());
    }

    protected Usuario getAuthenticationUser(){
        Principal authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return findByEmail(email);
    }

    protected Usuario findEntity(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id invalido"));
    }

    protected Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }
}
