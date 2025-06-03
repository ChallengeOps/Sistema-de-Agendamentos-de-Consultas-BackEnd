package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeAgendarDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.DisponibilidadeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DisponibilidadeService {

    private DisponibilidadeRepository disponibilidadeRepository;
    private UsuarioService usuarioService;
    private ServicoService servicoService;

    public DisponibilidadeService(DisponibilidadeRepository disponibilidadeRepository, UsuarioService usuarioService, ServicoService servicoService) {
        this.disponibilidadeRepository = disponibilidadeRepository;
        this.usuarioService = usuarioService;
        this.servicoService = servicoService;
    }

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public void criarDisponibilidade(DisponibilidadeDTO dto){
        var usuario = usuarioService.requireTokenUser();

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
    public DisponibilidadeAgendarDTO busacarPorServico(Integer id) {
        var servico = servicoService.findEntity(id);
        var disponibilidade = disponibilidadeRepository.findByProfissional(servico.getProfissional()).stream()
                .filter(d -> d.getHoraFim().isAfter(java.time.LocalDateTime.now()))
                .filter(d -> d.getAgendamento() == null )
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidade não encontrada"));
        return new DisponibilidadeAgendarDTO(
                disponibilidade.getId(),
                dateFormate(disponibilidade)
        );
    }


    @Transactional
    public List<Disponibilidade> listarPorProfissional(Integer id) {
        var usuario = usuarioService.findEntity(id);
        var agora = java.time.LocalDateTime.now();
        return disponibilidadeRepository.findByProfissional(usuario).stream()
                .filter(disponibilidade -> disponibilidade.getHoraFim().isAfter(agora))
                .toList();
    }


    private String dateFormate(Disponibilidade disponibilidade){
        //faca um metodo pra retornar a data nesse padrao 02/06/2025 (Segunda-feira) - 08:00 às 12:00\
        var diaDaSemana = disponibilidade.getDiaDeSemana().getValor();
        var horaInicio = disponibilidade.getHoraInicio().toLocalTime().toString();
        var horaFim = disponibilidade.getHoraFim().toLocalTime().toString();
        return String.format("%s - %s às %s", diaDaSemana, horaInicio, horaFim);
    }

    private Disponibilidade findEntity(Integer id) {
        return disponibilidadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidade não encontrada"));
    }

    public Disponibilidade findDisponibilidade(Integer integer) {
        return findEntity(integer);
    }
}
