package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoDetailsDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoListagemDTO;
import com.sistema_de_agendamentos.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoListagemDTO> criarServico(@RequestBody @Valid ServicoDTO body){
        var servico = servicoService.cadastrarServico(body);
        return ResponseEntity.ok(servico);
    }

    @GetMapping
    public ResponseEntity<List<ServicoListagemDTO>> listarTodos(){
        var servicos = servicoService.listarServicos();
        return ResponseEntity.ok(servicos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Integer id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }

}
