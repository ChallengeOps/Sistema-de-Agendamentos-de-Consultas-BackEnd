package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.repository.ProfissionalRepository;

public class ProfissionalService {

    private ProfissionalRepository profissionalRepository;

    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }


}
