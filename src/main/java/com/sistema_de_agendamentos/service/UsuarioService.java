package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.usuario.ProfissionaisDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioRegisterDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.mapper.UsuarioMapper;
import com.sistema_de_agendamentos.repository.UsuarioRepository;
import com.sistema_de_agendamentos.utils.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
        usuario.setAcesso(usuarioDTO.acesso());
        usuario.setPassword(usuarioDTO.senha());
        return usuarioRepository.save(usuario);
    }

    public List<UsuarioDTO> findAll() {
        var usuarios = usuarioRepository.findAll().stream()
                .map(usuarioMapper::fromEntity).toList();
        if (usuarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return usuarios;
    }

    @Transactional
    public UsuarioDTO findById(Integer id) {
        var usuario = findEntity(id);
        return usuarioMapper.fromEntity(usuario);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Integer id) {
        var usuario = findEntity(id);

        usuario.getAgendamentos().size();
        usuario.getServicos().size();
        usuario.getDisponibilidades().size();
        usuarioRepository.delete(usuario);

    }

    public List<ProfissionaisDTO> listarProfissionais() {
        var profissionais = usuarioRepository.findByAcesso(Usuario.ClienteTipo.PROFISSIONAL);
        if (profissionais.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum profissional cadastrado");
        }

        return profissionais.stream()
                .map(usuario ->
                    new ProfissionaisDTO(usuario.getId(),
                            usuario.getNome(),
                            usuario.getEmail()
                    )).toList();
    }



    public  Usuario requireTokenUser(){
        Principal principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String principalString = principal.getName();

        String email = StringUtil.extrairEmail(principalString);
        System.out.println("Email extraído: " + email);

        return findByEmail(email);
    }

    protected Usuario findEntity(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id invalido"));
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }
}