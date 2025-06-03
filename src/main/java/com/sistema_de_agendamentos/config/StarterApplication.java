package com.sistema_de_agendamentos.config;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioRegisterDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.entity.Servico;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.DisponibilidadeRepository;
import com.sistema_de_agendamentos.repository.ServicoRepository;
import com.sistema_de_agendamentos.service.DisponibilidadeService;
import com.sistema_de_agendamentos.service.ServicoService;
import com.sistema_de_agendamentos.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class StarterApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @PostConstruct
    public void start(){
        usuarioService.create(new UsuarioRegisterDTO( "Cliente Um", "cliente1@email.com",passwordEncoder.encode("123456"), Usuario.ClienteTipo.CLIENTE));
        usuarioService.create(new UsuarioRegisterDTO("Cliente Dois", "cliente2@email.com", passwordEncoder.encode("123456"), Usuario.ClienteTipo.CLIENTE));

        var profissional1 = usuarioService.create(new UsuarioRegisterDTO("Douglas Ribeiro", "profissional1@email.com", passwordEncoder.encode("123456") , Usuario.ClienteTipo.PROFISSIONAL));
        var profissional2 = usuarioService.create(new UsuarioRegisterDTO( "Gutemberg Chavez", "profissional2@email.com",passwordEncoder.encode("123456"), Usuario.ClienteTipo.PROFISSIONAL));


        var servico = new Servico();
        servico.setNome("Machine Learning");
        servico.setDescricao("Desenvolvimento de modelos de aprendizado de máquina e inteligência artificial.");
        servico.setProfissional(profissional1);
        servico.setDuracaoEmMinutos(30);
        servicoRepository.save(servico);

        var servico1 = new Servico();
        servico1.setNome("Desenvolvimento Web");
        servico1.setDescricao("Criação de sites e aplicações web responsivas.");
        servico1.setProfissional(profissional2);
        servico1.setDuracaoEmMinutos(180);
        servicoRepository.save(servico1);

        var servico2 = new Servico();
        servico2.setNome("Suporte Técnico");
        servico2.setDescricao("Atendimento para resolução de problemas em computadores e redes.");
        servico2.setProfissional(profissional1);
        servico2.setDuracaoEmMinutos(60);
        servicoRepository.save(servico2);

        var servico3 = new Servico();
        servico3.setNome("Consultoria em TI");
        servico3.setDescricao("Orientação para infraestrutura, segurança e melhores práticas em tecnologia.");
        servico3.setProfissional(profissional2);
        servico3.setDuracaoEmMinutos(60);
        servicoRepository.save(servico3);


// Disponibilidade para profissional1
        var disponibilidade1 = new Disponibilidade();
        disponibilidade1.setProfissional(profissional1);
        disponibilidade1.setHoraInicio(LocalDateTime.of(2025, 6, 2, 8, 0)); // 02/06/2025 08:00
        disponibilidade1.setHoraFim(LocalDateTime.of(2025, 6, 2, 12, 0));   // 02/06/2025 12:00
        disponibilidade1.setDiaDeSemana(Disponibilidade.DiaDeSemana.SEGUNDA);
        disponibilidadeRepository.save(disponibilidade1);

// Disponibilidade para profissional2
        var disponibilidade2 = new Disponibilidade();
        disponibilidade2.setProfissional(profissional2);
        disponibilidade2.setHoraInicio(LocalDateTime.of(2025, 6, 3, 14, 0)); // 03/06/2025 14:00
        disponibilidade2.setHoraFim(LocalDateTime.of(2025, 6, 3, 18, 0));    // 03/06/2025 18:00
        disponibilidade2.setDiaDeSemana(Disponibilidade.DiaDeSemana.TERCA);
        disponibilidadeRepository.save(disponibilidade2);

// Disponibilidade extra para profissional1
        var disponibilidade3 = new Disponibilidade();
        disponibilidade3.setProfissional(profissional1);
        disponibilidade3.setHoraInicio(LocalDateTime.of(2025, 6, 4, 9, 0)); // 04/06/2025 09:00
        disponibilidade3.setHoraFim(LocalDateTime.of(2025, 6, 4, 11, 0));   // 04/06/2025 11:00
        disponibilidade3.setDiaDeSemana(Disponibilidade.DiaDeSemana.QUARTA);
        disponibilidadeRepository.save(disponibilidade3);

// Disponibilidade extra para profissional2
        var disponibilidade4 = new Disponibilidade();
        disponibilidade4.setProfissional(profissional2);
        disponibilidade4.setHoraInicio(LocalDateTime.of(2025, 6, 5, 10, 0)); // 05/06/2025 10:00
        disponibilidade4.setHoraFim(LocalDateTime.of(2025, 6, 5, 13, 0));    // 05/06/2025 13:00
        disponibilidade4.setDiaDeSemana(Disponibilidade.DiaDeSemana.QUINTA);
        disponibilidadeRepository.save(disponibilidade4);

    }
}