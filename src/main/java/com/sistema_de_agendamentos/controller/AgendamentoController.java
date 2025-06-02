package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoCreateDTO;
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
    public ResponseEntity<String> criarAgendamento(AgendamentoCreateDTO createDTO) {
        agendamentoService.criarAgendamento(createDTO);
        return ResponseEntity.ok("Agendamento criado com sucesso");
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarAgendamentos() {
        List<Agendamento> agendamentos = agendamentoService.listarAgendamentosPorCliente();
        return ResponseEntity.ok(agendamentos);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarAgendamento(Integer id) {
        agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.ok("Agendamento cancelado com sucesso");
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<String> concluirAgendamento(Integer id) {
        agendamentoService.concluirAgendamento(id);
        return ResponseEntity.ok("Agendamento concluído com sucesso");
    }
}
