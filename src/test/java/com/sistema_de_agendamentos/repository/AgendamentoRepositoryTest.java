package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Agendamento;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.entity.Servico;
import com.sistema_de_agendamentos.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AgendamentoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    private Usuario cliente;
    private Usuario profissional;
    private Servico servico;
    private Disponibilidade disponibilidade;
    private Agendamento agendamento;

    @BeforeEach
    void setUp() {
        cliente = new Usuario();
        cliente.setNome("Cliente Teste");
        cliente.setEmail("cliente@test.com");
        cliente.setPassword("senha123");
        cliente.setAcesso(Usuario.ClienteTipo.CLIENTE);
        entityManager.persist(cliente);

        profissional = new Usuario();
        profissional.setNome("Dr. João");
        profissional.setEmail("profissional@test.com");
        profissional.setPassword("senha456");
        profissional.setAcesso(Usuario.ClienteTipo.PROFISSIONAL);
        entityManager.persist(profissional);

        servico = new Servico();
        servico.setNome("Consulta");
        servico.setDescricao("Consulta geral");
        servico.setDuracaoEmMinutos(30);
        servico.setProfissional(profissional);
        entityManager.persist(servico);

        disponibilidade = new Disponibilidade();
        disponibilidade.setHoraInicio(LocalDateTime.of(2025, 10, 20, 9, 0));
        disponibilidade.setHoraFim(LocalDateTime.of(2025, 10, 20, 10, 0));
        disponibilidade.setProfissional(profissional);
        entityManager.persist(disponibilidade);

        agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setDisponibilidade(disponibilidade);
        agendamento.setStatus(Agendamento.Status.PENDENTE);
        entityManager.persist(agendamento);

        entityManager.flush();
    }

    @Test
    void testFindById() {
        Optional<Agendamento> resultado = agendamentoRepository.findById(agendamento.getId());

        assertTrue(resultado.isPresent());
        assertEquals(Agendamento.Status.PENDENTE, resultado.get().getStatus());
        assertEquals("Cliente Teste", resultado.get().getCliente().getNome());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Agendamento> resultado = agendamentoRepository.findById(999);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testSaveAgendamento() {
        Agendamento novo = new Agendamento();
        novo.setCliente(cliente);
        novo.setProfissional(profissional);
        novo.setServico(servico);
        novo.setStatus(Agendamento.Status.CONFIRMADO);

        Agendamento salvo = agendamentoRepository.save(novo);

        assertNotNull(salvo.getId());
        assertEquals(Agendamento.Status.CONFIRMADO, salvo.getStatus());
    }

    @Test
    void testUpdateAgendamento() {
        agendamento.setStatus(Agendamento.Status.CONFIRMADO);

        Agendamento atualizado = agendamentoRepository.save(agendamento);

        assertEquals(Agendamento.Status.CONFIRMADO, atualizado.getStatus());
    }

    @Test
    void testDeleteAgendamento() {
        Integer id = agendamento.getId();
        agendamentoRepository.delete(agendamento);
        entityManager.flush();

        Optional<Agendamento> resultado = agendamentoRepository.findById(id);
        assertFalse(resultado.isPresent());
    }

    @Test
    void testFindAll() {
        List<Agendamento> resultado = agendamentoRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testAgendamentoRelations() {
        Optional<Agendamento> resultado = agendamentoRepository.findById(agendamento.getId());

        assertTrue(resultado.isPresent());
        Agendamento encontrado = resultado.get();
        
        assertNotNull(encontrado.getCliente());
        assertEquals("Cliente Teste", encontrado.getCliente().getNome());
        
        assertNotNull(encontrado.getProfissional());
        assertEquals("Dr. João", encontrado.getProfissional().getNome());
        
        assertNotNull(encontrado.getServico());
        assertEquals("Consulta", encontrado.getServico().getNome());
        
        assertNotNull(encontrado.getDisponibilidade());
        assertEquals(LocalDateTime.of(2025, 10, 20, 9, 0), encontrado.getDisponibilidade().getHoraInicio());
    }
}
