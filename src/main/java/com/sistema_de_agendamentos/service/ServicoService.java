package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.ServicoDTO;
import com.sistema_de_agendamentos.entity.Servico;

import com.sistema_de_agendamentos.repository.ServicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }


    /**
    @Transactional
    public Servico create(ServicoDTO dto){
        var servico = ServicoFactory.fromDTO(dto);
        return servicoRepository.save(servico);
    }

    public List<ServicoDTO> findAll(){
        var servicos = servicoRepository.findAll().stream()
                .map(ServicoFactory::fromServico).toList();
        if(servicos.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return servicos;
    }

    @Transactional
    public ServicoDTO findById(Integer id){
        var servico = findEntity(id);
        return ServicoFactory.fromServico(servico);
    }

    @Transactional
    public void deleteById(Integer id){
        findEntity(id);
        servicoRepository.deleteById(id);
    }

    public ServicoDTO updateServico(ServicoDTO dto, Integer id){
        var servico = findEntity(id);
        var updateServico = ServicoFactory.updateFromDTO(servico, dto);
        return ServicoFactory.fromServico(servicoRepository.save(updateServico));
    }

    private Servico findEntity(Integer id){
        return servicoRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id invalido"));
    }**/
}