package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Agendamento;
import com.sistema_de_agendamentos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    List<Agendamento> findByUsuario(Usuario usuario);
}
