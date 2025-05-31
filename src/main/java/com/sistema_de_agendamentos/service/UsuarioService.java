package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.UsuarioDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.mapper.UsuarioMapper;
import com.sistema_de_agendamentos.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public List<UsuarioDTO> findAll(){
        var usuarios = usuarioRepository.findAll().stream()
                .map(usuarioMapper::fromEntity).toList();
        if(usuarios.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return usuarios;
    }

    @Transactional
    public UsuarioDTO findById(Integer id){
        var usuario = findEntity(id);
        return usuarioMapper.fromEntity(usuario);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Integer id){
        findEntity(id);
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO updateUsuario(UsuarioDTO dto, Integer id){
        var usuario = findEntity(id);
        var updateUsuario = usuarioMapper.updateFromDTO(dto, usuario);
        return usuarioMapper.fromEntity(usuarioRepository.save(updateUsuario));
    }

    protected Usuario findEntity(Integer id){
        return usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id invalido"));
    }
}