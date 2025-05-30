package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissionalRepository extends JpaRepository<Profissional, Integer> {
}
