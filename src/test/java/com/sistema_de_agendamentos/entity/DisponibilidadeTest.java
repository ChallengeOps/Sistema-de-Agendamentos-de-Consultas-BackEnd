package com.sistema_de_agendamentos.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class DisponibilidadeTest {

    @Test
    void testDisponibilidadeCreation() {
        Disponibilidade disponibilidade = new Disponibilidade();
        LocalDateTime inicio = LocalDateTime.of(2025, 10, 20, 9, 0);
        LocalDateTime fim = LocalDateTime.of(2025, 10, 20, 10, 0);

        disponibilidade.setId(1);
        disponibilidade.setHoraInicio(inicio);
        disponibilidade.setHoraFim(fim);

        assertEquals(1, disponibilidade.getId());
        assertEquals(inicio, disponibilidade.getHoraInicio());
        assertEquals(fim, disponibilidade.getHoraFim());
    }

    @Test
    void testDisponibilidadeAllArgsConstructor() {
        Usuario profissional = new Usuario();
        profissional.setId(1);
        
        Agendamento agendamento = new Agendamento();
        agendamento.setId(1);

        LocalDateTime inicio = LocalDateTime.of(2025, 10, 21, 14, 0);
        LocalDateTime fim = LocalDateTime.of(2025, 10, 21, 15, 0);

        Disponibilidade disponibilidade = new Disponibilidade(
            1, inicio, fim, profissional, agendamento
        );

        assertEquals(1, disponibilidade.getId());
        assertEquals(inicio, disponibilidade.getHoraInicio());
        assertEquals(fim, disponibilidade.getHoraFim());
        assertEquals(profissional, disponibilidade.getProfissional());
        assertEquals(agendamento, disponibilidade.getAgendamento());
    }

    @Test
    void testDisponibilidadeNoArgsConstructor() {
        Disponibilidade disponibilidade = new Disponibilidade();
        assertNotNull(disponibilidade);
    }

    @Test
    void testDisponibilidadeSettersAndGetters() {
        Disponibilidade disponibilidade = new Disponibilidade();
        Usuario profissional = new Usuario();
        Agendamento agendamento = new Agendamento();
        
        LocalDateTime inicio = LocalDateTime.of(2025, 10, 22, 8, 30);
        LocalDateTime fim = LocalDateTime.of(2025, 10, 22, 9, 30);

        disponibilidade.setId(5);
        disponibilidade.setHoraInicio(inicio);
        disponibilidade.setHoraFim(fim);
        disponibilidade.setProfissional(profissional);
        disponibilidade.setAgendamento(agendamento);

        assertEquals(5, disponibilidade.getId());
        assertEquals(inicio, disponibilidade.getHoraInicio());
        assertEquals(fim, disponibilidade.getHoraFim());
        assertEquals(profissional, disponibilidade.getProfissional());
        assertEquals(agendamento, disponibilidade.getAgendamento());
    }

    @Test
    void testDisponibilidadeWithNullAgendamento() {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setAgendamento(null);
        assertNull(disponibilidade.getAgendamento());
    }

    @Test
    void testDisponibilidadeWithNullProfissional() {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setProfissional(null);
        assertNull(disponibilidade.getProfissional());
    }
}
