package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.controller.dto.ServicoDetailsDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoListagemDTO;
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

    @Transactional
    public Servico cadastrarServico(Integer id,ServicoDTO dto){

        Usuario usuario = usuarioService.findEntity(id);
        if (usuario.getAcesso() != Usuario.ClienteTipo.PROFISSIONAL){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Acesso restrito a funcionarios");
        }
        var servico = new Servico();
        servico.setProfissional(usuario);
        servico.setNome(dto.nome());
        servico.setDescricao(dto.descricao());
        servico.setDuracaoEmMinutos(dto.duracaoEmMinutos());

        return servicoRepository.save(servico);
    }

    @Transactional
    public List<ServicoListagemDTO> listarServicos() {
        //crie meu metodo e retorno o meu dto
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
                        .map(disponibilidade -> new DisponibilidadeListagemDTO(
                                disponibilidade.getId(),
                                disponibilidade.getHoraInicio(),
                                disponibilidade.getHoraFim()
                        ))
                        .toList()
        );
    }


    @Transactional
    public List<ServicoDTO> listarServicosPorProfissional(Integer id) {
        Usuario usuario = usuarioService.findEntity(id);
        if (usuario.getAcesso() != Usuario.ClienteTipo.PROFISSIONAL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Acesso restrito a funcionarios");
        }

        List<Servico> servicos = servicoRepository.findByProfissionalId(id);
        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum serviço encontrado para o profissional");
        }
        return servicos.stream()
                .map(servico -> new ServicoDTO(servico.getNome(), servico.getDescricao(), servico.getDuracaoEmMinutos()))
                .toList();
    }


    @Transactional
    public List<ServicoDTO> listarServicosPorProfissionalENome(Integer id, String nome) {
        Usuario usuario = usuarioService.findEntity(id);
        if (usuario.getAcesso() != Usuario.ClienteTipo.PROFISSIONAL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Acesso restrito a funcionarios");
        }

        List<Servico> servicos = servicoRepository.findByProfissionalIdAndNomeContainingIgnoreCase(id, nome);
        if (servicos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhum serviço encontrado para o profissional com o nome especificado");
        }
        return servicos.stream()
                .map(servico -> new ServicoDTO(servico.getNome(), servico.getDescricao(), servico.getDuracaoEmMinutos()))
                .toList();
    }

    @Transactional
    public void deleteServico(Integer id) {
        Servico servico = findEntity(id);
        servicoRepository.delete(servico);
    }

    private Servico findEntity(Integer id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    public Servico findServico(Integer integer) {
        return findEntity( integer);
    }
}