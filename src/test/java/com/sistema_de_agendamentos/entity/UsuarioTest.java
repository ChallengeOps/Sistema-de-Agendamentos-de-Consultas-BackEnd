package com.sistema_de_agendamentos.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class UsuarioTest {

    @Test
    void testUsuarioCreation() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@example.com");
        usuario.setPassword("senha123");
        usuario.setAcesso(Usuario.ClienteTipo.CLIENTE);

        assertEquals(1, usuario.getId());
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@example.com", usuario.getEmail());
        assertEquals("senha123", usuario.getPassword());
        assertEquals(Usuario.ClienteTipo.CLIENTE, usuario.getAcesso());
    }

    @Test
    void testUsuarioAllArgsConstructor() {
        List<Agendamento> agendamentos = new ArrayList<>();
        List<Servico> servicos = new ArrayList<>();
        List<Disponibilidade> disponibilidades = new ArrayList<>();

        Usuario usuario = new Usuario(
            1,
            "Maria Santos",
            "maria@example.com",
            "senha456",
            agendamentos,
            servicos,
            disponibilidades,
            Usuario.ClienteTipo.PROFISSIONAL
        );

        assertEquals(1, usuario.getId());
        assertEquals("Maria Santos", usuario.getNome());
        assertEquals("maria@example.com", usuario.getEmail());
        assertEquals("senha456", usuario.getPassword());
        assertEquals(Usuario.ClienteTipo.PROFISSIONAL, usuario.getAcesso());
        assertNotNull(usuario.getAgendamentos());
        assertNotNull(usuario.getServicos());
        assertNotNull(usuario.getDisponibilidades());
    }

    @Test
    void testUsuarioNoArgsConstructor() {
        Usuario usuario = new Usuario();
        assertNotNull(usuario);
    }

    @Test
    void testClienteTipoEnum() {
        assertEquals("CLIENTE", Usuario.ClienteTipo.CLIENTE.getNome());
        assertEquals("PROFISSIONAL", Usuario.ClienteTipo.PROFISSIONAL.getNome());
    }

    @Test
    void testUsuarioSettersAndGetters() {
        Usuario usuario = new Usuario();
        
        usuario.setId(10);
        usuario.setNome("Pedro");
        usuario.setEmail("pedro@test.com");
        usuario.setPassword("pass");
        usuario.setAcesso(Usuario.ClienteTipo.CLIENTE);

        List<Agendamento> agendamentos = new ArrayList<>();
        List<Servico> servicos = new ArrayList<>();
        List<Disponibilidade> disponibilidades = new ArrayList<>();
        
        usuario.setAgendamentos(agendamentos);
        usuario.setServicos(servicos);
        usuario.setDisponibilidades(disponibilidades);

        assertEquals(10, usuario.getId());
        assertEquals("Pedro", usuario.getNome());
        assertEquals("pedro@test.com", usuario.getEmail());
        assertEquals("pass", usuario.getPassword());
        assertEquals(Usuario.ClienteTipo.CLIENTE, usuario.getAcesso());
        assertEquals(agendamentos, usuario.getAgendamentos());
        assertEquals(servicos, usuario.getServicos());
        assertEquals(disponibilidades, usuario.getDisponibilidades());
    }
}
