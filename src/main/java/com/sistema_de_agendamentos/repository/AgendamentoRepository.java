package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
}
