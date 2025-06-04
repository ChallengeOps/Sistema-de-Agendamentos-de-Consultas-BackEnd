package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeAgendarDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.DisponibilidadeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.sistema_de_agendamentos.utils.DateFormaterUtils.dateFormate;

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

        // Montar LocalDateTime a partir de data + hora
        var data = LocalDate.parse(dto.data());  // "2025-06-06"
        var diaDeSemana = data.getDayOfWeek();  // Aqui você já sabe o dia da semana
        var horaInicio = LocalTime.parse(dto.horarioInicio());  // "16:09"
        var horaFim = LocalTime.parse(dto.horarioFim());        // "15:05"

        var inicio = LocalDateTime.of(data, horaInicio);
        var fim = LocalDateTime.of(data, horaFim);

        var agora = LocalDateTime.now();

        if (inicio.isBefore(agora) || fim.isBefore(agora)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Horários não podem estar no passado");
        }
        if (!fim.isAfter(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hora de fim deve ser após hora de início");
        }

        var disponibilidade = new Disponibilidade();
        disponibilidade.setProfissional(usuario);
        disponibilidade.setHoraInicio(inicio);
        disponibilidade.setHoraFim(fim);

        disponibilidadeRepository.save(disponibilidade);
    }

    @Transactional
    public List<DisponibilidadeAgendarDTO> buscarPorServico(Integer id) {
        var servico = servicoService.findEntity(id);
        var agora = java.time.LocalDateTime.now();
        return disponibilidadeRepository.findByProfissional(servico.getProfissional()).stream()
                .filter(d -> d.getHoraFim().isAfter(agora))
                .filter(d -> d.getAgendamento() == null)
                .map(d -> new DisponibilidadeAgendarDTO(
                        d.getId(),
                        dateFormate(d)
                ))
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public List<DisponibilidadeListagemDTO> listarPorProfissional() {
        var usuario = usuarioService.requireTokenUser();
        var agora = java.time.LocalDateTime.now();
        return disponibilidadeRepository.findByProfissional(usuario).stream()
                .filter(d -> d.getHoraFim().isAfter(agora))
                .filter(d -> d.getAgendamento() == null)
                .map(d -> new DisponibilidadeListagemDTO(
                        d.getId(),
                        d.getHoraInicio().toLocalDate().toString(),
                        d.getHoraInicio().toLocalTime().toString(),
                        d.getHoraFim().toLocalTime().toString()
                ))
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public void delete(Integer id) {
        var disponibilidade = findEntityPermission(id);
        disponibilidadeRepository.delete(disponibilidade);
    }


    protected Disponibilidade findEntity(Integer id) {
        return disponibilidadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidade não encontrada"));
    }

    protected Disponibilidade findEntityPermission(Integer id) {
        var disponibilidade = findEntity(id);
        var usuario = usuarioService.requireTokenUser();
        if (!disponibilidade.getProfissional().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta disponibilidade");
        }
        return disponibilidade;
    }
}
