package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.DisponibilidadeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DisponibilidadeService {

    private DisponibilidadeRepository disponibilidadeRepository;
    private UsuarioService usuarioService;

    public DisponibilidadeService(DisponibilidadeRepository disponibilidadeRepository, UsuarioService usuarioService) {
        this.disponibilidadeRepository = disponibilidadeRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public void criarDisponibilidade(Integer id, DisponibilidadeDTO dto){
        var usuario = usuarioService.findEntity(id);

        var agora = java.time.LocalDateTime.now();

        if (dto.horaInicio().isBefore(agora) || dto.horaFim().isBefore(agora)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Horários não podem estar no passado");
        }
        if (!dto.horaFim().isAfter(dto.horaInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hora de fim deve ser após hora de início");
        }

        var disponibilidade = new Disponibilidade();
        disponibilidade.setProfissional(usuario);
        disponibilidade.setHoraInicio(dto.horaInicio());
        disponibilidade.setHoraFim(dto.horaFim());
        disponibilidade.setDiaDeSemana(Disponibilidade.DiaDeSemana.fromString(dto.diaDeSemana()));

        disponibilidadeRepository.save(disponibilidade);
    }


    @Transactional
    public List<Disponibilidade> listarPorProfissional(Integer id) {
        var usuario = usuarioService.findEntity(id);
        var agora = java.time.LocalDateTime.now();
        return disponibilidadeRepository.findByProfissional(usuario).stream()
                .filter(disponibilidade -> disponibilidade.getHoraFim().isAfter(agora))
                .toList();
    }



    private Disponibilidade findEntity(Integer id) {
        return disponibilidadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidade não encontrada"));
    }

    public Disponibilidade findDisponibilidade(Integer integer) {
        return findEntity(integer);
    }
}
