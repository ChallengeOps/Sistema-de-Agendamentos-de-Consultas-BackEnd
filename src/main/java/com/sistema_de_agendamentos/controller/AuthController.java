package com.sistema_de_agendamentos.controller;


import com.sistema_de_agendamentos.config.security.AuthService;
import com.sistema_de_agendamentos.config.security.dto.LoginRequestDTO;
import com.sistema_de_agendamentos.config.security.dto.RegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        var login = authService.login(body);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/register")
    public ResponseEntity registerUsuario(@RequestBody RegisterRequestDTO body){
        var register = authService.registerUsuario(body);
        return ResponseEntity.ok(register);
    }

    @PostMapping("/registerProfissional")
    public ResponseEntity registerProfissional(@RequestBody RegisterRequestDTO body){
        var register = authService.registerProfissional(body);
        return ResponseEntity.ok(register);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity registerAdmin(@RequestBody RegisterRequestDTO body){
        var register = authService.registerAdmin(body);
        return ResponseEntity.ok(register);
    }


}