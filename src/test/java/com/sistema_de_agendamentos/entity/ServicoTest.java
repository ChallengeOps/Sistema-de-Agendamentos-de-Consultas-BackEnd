package com.sistema_de_agendamentos.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class ServicoTest {

    @Test
    void testServicoCreation() {
        Servico servico = new Servico();
        servico.setId(1);
        servico.setNome("Consulta Médica");
        servico.setDescricao("Consulta clínica geral");
        servico.setDuracaoEmMinutos(30);

        assertEquals(1, servico.getId());
        assertEquals("Consulta Médica", servico.getNome());
        assertEquals("Consulta clínica geral", servico.getDescricao());
        assertEquals(30, servico.getDuracaoEmMinutos());
    }

    @Test
    void testServicoAllArgsConstructor() {
        Usuario profissional = new Usuario();
        profissional.setId(1);
        profissional.setNome("Dr. João");

        List<Agendamento> agendamentos = new ArrayList<>();

        Servico servico = new Servico(
            1,
            "Terapia",
            "Sessão de terapia",
            60,
            profissional,
            agendamentos
        );

        assertEquals(1, servico.getId());
        assertEquals("Terapia", servico.getNome());
        assertEquals("Sessão de terapia", servico.getDescricao());
        assertEquals(60, servico.getDuracaoEmMinutos());
        assertEquals(profissional, servico.getProfissional());
        assertNotNull(servico.getAgendamentos());
    }

    @Test
    void testServicoNoArgsConstructor() {
        Servico servico = new Servico();
        assertNotNull(servico);
    }

    @Test
    void testServicoSettersAndGetters() {
        Servico servico = new Servico();
        Usuario profissional = new Usuario();
        profissional.setId(2);
        
        servico.setId(10);
        servico.setNome("Massagem");
        servico.setDescricao("Massagem relaxante");
        servico.setDuracaoEmMinutos(45);
        servico.setProfissional(profissional);

        List<Agendamento> agendamentos = new ArrayList<>();
        servico.setAgendamentos(agendamentos);

        assertEquals(10, servico.getId());
        assertEquals("Massagem", servico.getNome());
        assertEquals("Massagem relaxante", servico.getDescricao());
        assertEquals(45, servico.getDuracaoEmMinutos());
        assertEquals(profissional, servico.getProfissional());
        assertEquals(agendamentos, servico.getAgendamentos());
    }

    @Test
    void testServicoWithNullProfissional() {
        Servico servico = new Servico();
        servico.setProfissional(null);
        assertNull(servico.getProfissional());
    }

    @Test
    void testServicoWithNullAgendamentos() {
        Servico servico = new Servico();
        servico.setAgendamentos(null);
        assertNull(servico.getAgendamentos());
    }
}
