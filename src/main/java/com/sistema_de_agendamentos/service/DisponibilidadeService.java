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
