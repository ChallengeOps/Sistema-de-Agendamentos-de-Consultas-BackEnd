package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeAgendarDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.mapper.DisponibilidadeMapper;
import com.sistema_de_agendamentos.repository.DisponibilidadeRepository;
import com.sistema_de_agendamentos.utils.DateFormaterUtils;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DisponibilidadeService {

    private final DisponibilidadeRepository disponibilidadeRepository;
    private final UsuarioService usuarioService;
    private final ServicoService servicoService;
    private final DisponibilidadeMapper disponibilidadeMapper;

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public void criarDisponibilidade(DisponibilidadeDTO dto){
        var usuario = usuarioService.getAuthenticationUser();

        var datas = DateFormaterUtils.extrairDatas(dto);
        var inicio = datas.inicio;
        var fim = datas.fim;
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
                .map(d -> disponibilidadeMapper.toAgendarDTO(d))
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public List<DisponibilidadeListagemDTO> listarPorProfissional() {
        var usuario = usuarioService.getAuthenticationUser();
        var agora = java.time.LocalDateTime.now();
        return disponibilidadeRepository.findByProfissional(usuario).stream()
                .filter(d -> d.getHoraFim().isAfter(agora))
                .filter(d -> d.getAgendamento() == null)
                .map(d -> disponibilidadeMapper.toListagemDTO(d))
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
        var usuario = usuarioService.getAuthenticationUser();
        if (!disponibilidade.getProfissional().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta disponibilidade");
        }
        return disponibilidade;
    }
}
