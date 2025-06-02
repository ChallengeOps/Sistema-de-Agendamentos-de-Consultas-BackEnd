package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        usuarioService.deleteById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping
    public String var() {
        Usuario user = usuarioService.requireTokenUser();
        return "Olá, " + user.getNome() + "! Você está autenticado como " + user.getEmail() + ".";
    }

}
