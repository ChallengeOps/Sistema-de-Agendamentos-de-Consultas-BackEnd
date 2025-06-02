package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoCreateDTO;
import com.sistema_de_agendamentos.entity.Agendamento;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.AgendamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AgendamentoService {

    private AgendamentoRepository agendamentoRepository;
    private UsuarioService usuarioService;
    private DisponibilidadeService disponibilidadeService;
    private ServicoService servicoService;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, UsuarioService usuarioService,
                              DisponibilidadeService disponibilidadeService, ServicoService servicoService) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioService = usuarioService;
        this.disponibilidadeService = disponibilidadeService;
        this.servicoService = servicoService;
    }

    @Transactional
    @PreAuthorize("hasRole('CLIENTE')")
    public void criarAgendamento(AgendamentoCreateDTO createDTO){
        System.out.println("Criando agendamento com DTO: " + createDTO);
        var usuario = usuarioService.requireTokenUser();
        System.out.println(usuario.getNome());
        var profissional = usuarioService.findEntity(createDTO.profissionalId());
        var disponibilidade = disponibilidadeService.findDisponibilidade(createDTO.disponibilidadeId());
        var servico = servicoService.findServico(createDTO.servicoId());

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
    public List<Agendamento> listarAgendamentosPorCliente() {
        var usuario = usuarioService.requireTokenUser();
        if(usuario.getAcesso() == Usuario.ClienteTipo.PROFISSIONAL) {
            return agendamentoRepository.findByProfissional(usuario);
        }
        if (usuario.getAcesso() == Usuario.ClienteTipo.CLIENTE) {
            return agendamentoRepository.findByCliente(usuario);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
    }

    public void cancelarAgendamento(Integer id) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Agendamento não encontrado"));

        if (agendamento.getStatus() == Agendamento.Status.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamento já cancelado");
        }

        agendamento.setStatus(Agendamento.Status.CANCELADO);
        agendamentoRepository.save(agendamento);
    }

    public void concluirAgendamento(Integer id) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Agendamento não encontrado"));

        if (agendamento.getStatus() == Agendamento.Status.CONCLUIDO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamento já concluído");
        }

        agendamento.setStatus(Agendamento.Status.CONCLUIDO);
        agendamentoRepository.save(agendamento);
    }

    public void agendamentoSetStatus(Integer id, String status) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Agendamento não encontrado"));

        if (!status.equals("CONFIRMADO") && !status.equals("CANCELADO")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido");
        }
        agendamento.setStatus(Agendamento.Status.valueOf(status));
        agendamentoRepository.save(agendamento);
    }


}
