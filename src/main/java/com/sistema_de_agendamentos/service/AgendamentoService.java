package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoCreateDTO;
import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoDTO;
import com.sistema_de_agendamentos.entity.Agendamento;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.AgendamentoRepository;
import com.sistema_de_agendamentos.utils.DateFormaterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioService usuarioService;
    private final DisponibilidadeService disponibilidadeService;
    private final ServicoService servicoService;

    @Transactional
    @PreAuthorize("hasRole('CLIENTE')")
    public void criarAgendamento(AgendamentoCreateDTO createDTO){
        System.out.println("Criando agendamento com DTO: " + createDTO);
        var usuario = usuarioService.requireTokenUser();

        System.out.println(usuario.getNome());
        var servico = servicoService.findEntity(createDTO.servicoId());
        var profissional = usuarioService.findEntity(servico.getProfissional().getId());
        var disponibilidade = disponibilidadeService.findEntity(createDTO.disponibilidadeId());

        if (!disponibilidade.getProfissional().equals(servico.getProfissional())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Serviço e disponibilidade devem pertencer ao mesmo profissional");
        }

        var agendamento = new Agendamento();
        agendamento.setProfissional(profissional);
        agendamento.setCliente(usuario);
        agendamento.setDisponibilidade(disponibilidade);
        agendamento.setServico(servico);
        agendamento.setStatus(Agendamento.Status.PENDENTE);

        agendamentoRepository.save(agendamento);
    }

    @Transactional
    public List<AgendamentoDTO> listarAgendamentosPorCliente() {
        var usuario = usuarioService.requireTokenUser();
        var agendamentos = usuario.getAgendamentos().stream()
            .filter(p -> p.getStatus() == Agendamento.Status.PENDENTE)
            .map(p -> new AgendamentoDTO(
                p.getId(),
                p.getProfissional().getNome(),
                p.getServico().getNome(),
                DateFormaterUtils.dateFormate(p.getDisponibilidade())
            ))
            .toList();

        if (agendamentos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum agendamento encontrado");
        }
        return agendamentos;
    }

    public void delete(Integer id) {
       var agendamento = findEntityPermission(id);
       agendamento.setDisponibilidade(null);
       agendamento.setStatus(Agendamento.Status.CANCELADO);
       agendamentoRepository.save(agendamento);
       agendamentoRepository.deleteById(id);
    }

    public Agendamento findEntityPermission(Integer id) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));
        var usuario = usuarioService.requireTokenUser();
        if (!agendamento.getCliente().equals(usuario) && !agendamento.getProfissional().equals(usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }
        return agendamento;
    }

}
