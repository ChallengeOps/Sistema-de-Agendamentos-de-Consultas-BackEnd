package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoDetailsDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoListagemDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.ProfissionaisDTO;
import com.sistema_de_agendamentos.entity.Servico;

import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicoService {

    private ServicoRepository servicoRepository;
    private UsuarioService usuarioService;

    public ServicoService(ServicoRepository servicoRepository, UsuarioService usuarioService) {
        this.servicoRepository = servicoRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ServicoListagemDTO cadastrarServico(ServicoDTO dto){

        Usuario profissional = usuarioService.requireTokenUser();

        var servico = new Servico();
        servico.setProfissional(profissional);
        servico.setNome(dto.nome());
        servico.setDescricao(dto.descricao());
        servico.setDuracaoEmMinutos(dto.duracaoEmMinutos());

        return new ServicoListagemDTO(
                servicoRepository.save(servico).getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getDuracaoEmMinutos(),
                profissional.getNome()
        );
    }

    @Transactional
    public List<ServicoListagemDTO> listarServicos() {
        List<Servico> servicos = servicoRepository.findAll();
        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum serviço cadastrado");
        }
        return servicos.stream()
                .map(servico -> new ServicoListagemDTO(servico.getId(),
                        servico.getNome(),
                        servico.getDescricao(),
                        servico.getDuracaoEmMinutos(), servico.getProfissional().getNome()))
                .toList();
    }



    public ServicoDetailsDTO detalharServico(Integer id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));

        return new ServicoDetailsDTO(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getDuracaoEmMinutos(),
                servico.getProfissional().getNome(),
                servico.getProfissional().getDisponibilidades().stream()
                        .filter(disponibilidade -> disponibilidade.getAgendamento() == null)
                        .map(disponibilidade -> new DisponibilidadeListagemDTO(
                                disponibilidade.getId(),
                                disponibilidade.getHoraInicio(),
                                disponibilidade.getHoraFim()
                        ))
                        .toList()
        );
    }


    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public List<ServicoDTO> listarServicosPorProfissional() {
        Usuario usuario = usuarioService.requireTokenUser();

        List<Servico> servicos = servicoRepository.findByProfissionalId(usuario.getId());
        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum serviço encontrado para o profissional");
        }
        return servicos.stream()
                .map(servico -> new ServicoDTO(servico.getNome(), servico.getDescricao(), servico.getDuracaoEmMinutos()))
                .toList();
    }


    public ProfissionaisDTO getProfissionalByServico(Integer id) {
        Servico servico = findEntity(id);
        Usuario profissional = servico.getProfissional();
        return new ProfissionaisDTO(profissional.getId(), profissional.getNome(), profissional.getEmail());
    }



    private Servico findEntity(Integer id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    public Servico findServico(Integer integer) {
        return findEntity( integer);
    }
}