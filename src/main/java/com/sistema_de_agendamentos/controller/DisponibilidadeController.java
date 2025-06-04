package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeAgendarDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.service.DisponibilidadeService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> criarDisponibilidade(@RequestBody @Valid DisponibilidadeDTO dto) {
        disponibilidadeService.criarDisponibilidade(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profissional")
    public ResponseEntity<List<DisponibilidadeListagemDTO>> listarPorProfissional() {
        var disponibilidades = disponibilidadeService.listarPorProfissional();
        return ResponseEntity.ok(disponibilidades);
    }

    @GetMapping("/servico/{id}")
    public ResponseEntity<List<DisponibilidadeAgendarDTO>> buscarPorServico(@PathVariable Integer id) {
        var dto = disponibilidadeService.buscarPorServico(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisponibilidade(@PathVariable Integer id) {
        disponibilidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
