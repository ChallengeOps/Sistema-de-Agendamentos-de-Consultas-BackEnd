package com.sistema_de_agendamentos.controller;

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

    @GetMapping("/servicos")
    private ResponseEntity<List> listarServicosPorProfissional() {
        var servicos = servicoService.listarServicosPorProfissional();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionaisDTO>> listarProfissionais() {
        var profissionais = usuarioService.listarProfissionais();
        return ResponseEntity.ok(profissionais);
    }

    public ResponseEntity<List<Disponibilidade> > listarDisponibilidadesPorProfissional(Integer id) {
        var disponibilidades = d(id);
        return ResponseEntity.ok(disponibilidades);
    }
}
