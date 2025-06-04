package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
}
