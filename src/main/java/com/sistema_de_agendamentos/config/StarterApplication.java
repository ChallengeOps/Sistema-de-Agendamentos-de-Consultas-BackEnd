package com.sistema_de_agendamentos.config;

import com.sistema_de_agendamentos.controller.dto.DisponibilidadeDTO;
import com.sistema_de_agendamentos.controller.dto.ServicoDTO;
import com.sistema_de_agendamentos.controller.dto.UsuarioDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.service.DisponibilidadeService;
import com.sistema_de_agendamentos.service.ServicoService;
import com.sistema_de_agendamentos.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class StarterApplication {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    @Autowired
    private ServicoService servicoService;

    @PostConstruct
    public void start(){
        usuarioService.create(new UsuarioDTO(null, "Cliente Um", "cliente1@email.com", Usuario.ClienteTipo.CLIENTE));
        usuarioService.create(new UsuarioDTO(null, "Cliente Dois", "cliente2@email.com",  Usuario.ClienteTipo.CLIENTE));

        usuarioService.create(new UsuarioDTO(null, "Profissional Um", "profissional1@email.com",Usuario.ClienteTipo.PROFISSIONAL));
        usuarioService.create(new UsuarioDTO(null, "Profissional Dois", "profissional2@email.com", Usuario.ClienteTipo.PROFISSIONAL));

        // Buscar profissionais pelo e-mail
        var profissional1 = usuarioService.findByEmail("profissional1@email.com");
        var profissional2 = usuarioService.findByEmail("profissional2@email.com");

        // Disponibilidades para Profissional 1
        disponibilidadeService.criarDisponibilidade(
                profissional1.getId(),
                new DisponibilidadeDTO("SEGUNDA", LocalDateTime.of(2024, 6, 10, 8, 0), LocalDateTime.of(2024, 6, 10, 12, 0))
        );
        servicoService.cadastrarServico(
                profissional1.getId(),
                new ServicoDTO("Corte de Cabelo", "Corte masculino", 30)
        );
    }
}