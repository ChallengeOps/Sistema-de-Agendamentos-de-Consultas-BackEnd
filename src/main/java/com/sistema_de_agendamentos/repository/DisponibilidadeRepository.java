package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Disponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Integer> {
}
