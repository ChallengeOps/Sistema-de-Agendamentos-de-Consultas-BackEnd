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


    private Servico findEntity(Integer id){
        return servicoRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id invalido"));
    }
}