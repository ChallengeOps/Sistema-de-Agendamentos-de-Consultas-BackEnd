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
        Usuario profissional = usuarioService.requireTokenUser();
        var servico = servicoMapper.fromDTO(dto, profissional);
        return new ServicoListagemDTO(
                servicoRepository.save(servico).getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getDuracaoEmMinutos(),
                profissional.getNome()
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

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public void delete(Integer id) {
        Servico servico = findEntityPermission(id);
        servicoRepository.delete(servico);
    }

    @Transactional
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public void update(Integer id, ServicoDTO dto) {
        Servico servico = findEntityPermission(id);
        servicoMapper.updateFromDTO(dto, servico);
        servicoRepository.save(servico);
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



    protected Servico findEntity(Integer id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    protected Servico findEntityPermission(Integer id) {
        Usuario usuario = usuarioService.requireTokenUser();
        var servico = findEntity(id);
        if (!servico.getProfissional().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este serviço");
        }
        return servico;
    }
}