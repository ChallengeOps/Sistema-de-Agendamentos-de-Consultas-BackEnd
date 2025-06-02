package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Integer> {
    List<Disponibilidade> findByProfissional(Usuario usuario);
}
