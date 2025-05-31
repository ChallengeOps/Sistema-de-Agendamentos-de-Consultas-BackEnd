package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.ServicoDTO;
import com.sistema_de_agendamentos.entity.Servico;

import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
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

    // funcao de cadastrar serviço
    public void cadastrarServico(Integer id,ServicoDTO dto){

        Usuario usuario = usuarioService.findEntity(id);
        if (usuario.getAcesso() != Usuario.ClienteTipo.PROFISSIONAL){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Acesso restrito a funcionarios");
        }
        var servico = new Servico();
        servico.setProfissional(usuario);
        servico.setNome(dto.nome());
        servico.setDescricao(dto.descricao());
        servico.setDuracaoEmMinutos(dto.duracaoEmMinutos());
        servicoRepository.save(servico);
    }
}