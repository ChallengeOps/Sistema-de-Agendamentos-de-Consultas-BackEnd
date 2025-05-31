package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.service.UsuarioService;

public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


}
