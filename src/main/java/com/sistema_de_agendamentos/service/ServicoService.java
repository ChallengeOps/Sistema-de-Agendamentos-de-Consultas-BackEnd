package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoDetailsDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoListagemDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.ProfissionaisDTO;
import com.sistema_de_agendamentos.entity.Servico;

import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.mapper.ServicoMapper;
import com.sistema_de_agendamentos.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final UsuarioService usuarioService;
    private final ServicoMapper servicoMapper;

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ServicoListagemDTO cadastrarServico(ServicoDTO dto){
        Usuario profissional = usuarioService.getAuthenticationUser();
        var servico = servicoMapper.fromDTO(dto, profissional);
        servicoRepository.save(servico);
        return servicoMapper.toListagemDTO(servico);
    }

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public List<ServicoListagemDTO> listarServicosPorProfissional() {
        Usuario usuario = usuarioService.getAuthenticationUser();
        List<Servico> servicos = usuario.getServicos();
        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum serviço encontrado para o profissional");
        }
        return servicos.stream().map(s -> servicoMapper.toListagemDTO(s)).toList();
    }

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public void deletarServico(Integer id) {
        Servico servico = findEntityWithPermission(id);
        servicoRepository.delete(servico);
    }

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ServicoListagemDTO update(Integer id, ServicoListagemDTO dto) {
        Servico servico = findEntityWithPermission(id);
        servicoMapper.updateFromListagemDTO(servico, dto);
        servicoRepository.save(servico);
        return servicoMapper.toListagemDTO(servico);
    }


    @Transactional
    public List<ServicoListagemDTO> listarServicos() {
        List<Servico> servicos = servicoRepository.findAll();
        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum serviço cadastrado");
        }
        return servicos.stream()
                .map(servico -> servicoMapper.toListagemDTO(servico)).toList();
    }

    protected Servico findEntity(Integer id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    protected Servico findEntityWithPermission(Integer id) {
        Usuario usuario = usuarioService.getAuthenticationUser();
        var servico = findEntity(id);
        if (!servico.getProfissional().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este serviço");
        }
        return servico;
    }
}