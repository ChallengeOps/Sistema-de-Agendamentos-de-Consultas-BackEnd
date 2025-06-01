package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.service.UsuarioService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

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
    public String var(Principal principal) {
        String principalString = principal.getName();
        System.out.println("Principal bruto: " + principalString);

        // Extrai o email de dentro da String
        String email = extrairEmail(principalString);
        System.out.println("Email extraído: " + email);

        Usuario user = usuarioService.findByEmail(email);
        return "Olá, " + user.getNome() + "! Você está autenticado como " + user.getEmail() + ".";
    }



    private String extrairEmail(String principalString) {
        int emailIndex = principalString.indexOf("email=");
        if (emailIndex == -1) {
            throw new RuntimeException("Email não encontrado na String do principal!");
        }
        int start = emailIndex + "email=".length();
        int end = principalString.indexOf(",", start);
        if (end == -1) {
            end = principalString.length();
        }
        return principalString.substring(start, end).trim();
    }


}
