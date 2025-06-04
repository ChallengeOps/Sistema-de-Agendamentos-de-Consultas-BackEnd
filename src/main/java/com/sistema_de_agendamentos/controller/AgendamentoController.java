package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoCreateDTO;
import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoDTO;
import com.sistema_de_agendamentos.entity.Agendamento;
import com.sistema_de_agendamentos.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<String> criarAgendamento(@RequestBody AgendamentoCreateDTO createDTO) {
        agendamentoService.criarAgendamento(createDTO);
        return ResponseEntity.ok("Agendamento criado com sucesso");
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoDTO>> listarAgendamentos() {
        List<AgendamentoDTO> agendamentos = agendamentoService.listarAgendamentosPorCliente();
        return ResponseEntity.ok(agendamentos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgendamento(@PathVariable Integer id) {
        agendamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
