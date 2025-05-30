package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {}
