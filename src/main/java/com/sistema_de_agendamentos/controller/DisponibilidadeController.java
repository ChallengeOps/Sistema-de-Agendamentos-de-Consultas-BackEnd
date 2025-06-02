package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.service.DisponibilidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadeController {

    private DisponibilidadeService disponibilidadeService;

    public DisponibilidadeController(DisponibilidadeService disponibilidadeService) {
        this.disponibilidadeService = disponibilidadeService;
    }

    @PostMapping
    public ResponseEntity<Void> criarDisponibilidade(@RequestBody DisponibilidadeDTO dto) {
        disponibilidadeService.criarDisponibilidade(dto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/profissional/{id}")
    public ResponseEntity<List<Disponibilidade>> listarPorProfissional(@PathVariable Integer id) {
        var disponibilidades = disponibilidadeService.listarPorProfissional(id);
        return ResponseEntity.ok(disponibilidades);
    }
}
