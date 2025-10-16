package com.sistema_de_agendamentos.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgendamentoTest {

    @Test
    void testAgendamentoCreation() {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(1);
        agendamento.setStatus(Agendamento.Status.PENDENTE);

        assertEquals(1, agendamento.getId());
        assertEquals(Agendamento.Status.PENDENTE, agendamento.getStatus());
    }

    @Test
    void testAgendamentoAllArgsConstructor() {
        Usuario cliente = new Usuario();
        cliente.setId(1);
        
        Usuario profissional = new Usuario();
        profissional.setId(2);
        
        Servico servico = new Servico();
        servico.setId(1);
        
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setId(1);

        Agendamento agendamento = new Agendamento(
            1, cliente, profissional, servico, disponibilidade, Agendamento.Status.CONFIRMADO
        );

        assertEquals(1, agendamento.getId());
        assertEquals(cliente, agendamento.getCliente());
        assertEquals(profissional, agendamento.getProfissional());
        assertEquals(servico, agendamento.getServico());
        assertEquals(disponibilidade, agendamento.getDisponibilidade());
        assertEquals(Agendamento.Status.CONFIRMADO, agendamento.getStatus());
    }

    @Test
    void testStatusFromStringValid() {
        assertEquals(Agendamento.Status.PENDENTE, Agendamento.Status.fromString("PENDENTE"));
        assertEquals(Agendamento.Status.CONFIRMADO, Agendamento.Status.fromString("CONFIRMADO"));
        assertEquals(Agendamento.Status.CANCELADO, Agendamento.Status.fromString("CANCELADO"));
        assertEquals(Agendamento.Status.CONCLUIDO, Agendamento.Status.fromString("CONCLUIDO"));
    }

    @Test
    void testStatusFromStringCaseInsensitive() {
        assertEquals(Agendamento.Status.PENDENTE, Agendamento.Status.fromString("pendente"));
        assertEquals(Agendamento.Status.CONFIRMADO, Agendamento.Status.fromString("confirmado"));
        assertEquals(Agendamento.Status.CANCELADO, Agendamento.Status.fromString("cancelado"));
        assertEquals(Agendamento.Status.CONCLUIDO, Agendamento.Status.fromString("concluido"));
    }

    @Test
    void testStatusFromStringWithSpaces() {
        assertEquals(Agendamento.Status.PENDENTE, Agendamento.Status.fromString("  PENDENTE  "));
    }

    @Test
    void testStatusFromStringNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Agendamento.Status.fromString(null)
        );
        assertEquals("Status não pode ser nulo", exception.getMessage());
    }

    @Test
    void testStatusFromStringInvalid() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Agendamento.Status.fromString("INVALIDO")
        );
        assertTrue(exception.getMessage().contains("Status inválido"));
    }

    @Test
    void testStatusFromStringEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Agendamento.Status.fromString("")
        );
        assertTrue(exception.getMessage().contains("Status inválido"));
    }

    @Test
    void testAgendamentoSettersAndGetters() {
        Agendamento agendamento = new Agendamento();
        Usuario cliente = new Usuario();
        Usuario profissional = new Usuario();
        Servico servico = new Servico();
        Disponibilidade disponibilidade = new Disponibilidade();

        agendamento.setId(5);
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setDisponibilidade(disponibilidade);
        agendamento.setStatus(Agendamento.Status.CONCLUIDO);

        assertEquals(5, agendamento.getId());
        assertEquals(cliente, agendamento.getCliente());
        assertEquals(profissional, agendamento.getProfissional());
        assertEquals(servico, agendamento.getServico());
        assertEquals(disponibilidade, agendamento.getDisponibilidade());
        assertEquals(Agendamento.Status.CONCLUIDO, agendamento.getStatus());
    }
}
