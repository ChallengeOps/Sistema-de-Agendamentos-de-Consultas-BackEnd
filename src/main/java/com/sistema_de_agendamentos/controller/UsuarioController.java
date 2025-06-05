package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioPerfilDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioPerfilDTO> getPerfil() {
        return ResponseEntity.ok(usuarioService.me());
    }

}
