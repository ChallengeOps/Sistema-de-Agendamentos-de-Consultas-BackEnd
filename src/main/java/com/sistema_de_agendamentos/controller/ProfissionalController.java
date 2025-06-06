package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.controller.dto.servico.ServicoListagemDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.ProfissionaisDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.service.ServicoService;
import com.sistema_de_agendamentos.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    private UsuarioService usuarioService;
    private ServicoService servicoService;

    public ProfissionalController(UsuarioService usuarioService, ServicoService servicoService) {
        this.usuarioService = usuarioService;
        this.servicoService = servicoService;
    }

    @GetMapping("/servicos")
    private ResponseEntity<List<ServicoListagemDTO>> listarServicosPorProfissional() {
        var servicos = servicoService.listarServicosPorProfissional();
        return ResponseEntity.ok(servicos);
    }

}
