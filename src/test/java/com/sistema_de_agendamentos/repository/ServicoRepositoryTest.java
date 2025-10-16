package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Servico;
import com.sistema_de_agendamentos.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ServicoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ServicoRepository servicoRepository;

    private Usuario profissional;
    private Servico servico1;
    private Servico servico2;

    @BeforeEach
    void setUp() {
        profissional = new Usuario();
        profissional.setNome("Dr. João");
        profissional.setEmail("joao@test.com");
        profissional.setPassword("senha123");
        profissional.setAcesso(Usuario.ClienteTipo.PROFISSIONAL);
        entityManager.persist(profissional);

        servico1 = new Servico();
        servico1.setNome("Consulta Médica");
        servico1.setDescricao("Consulta geral");
        servico1.setDuracaoEmMinutos(30);
        servico1.setProfissional(profissional);
        entityManager.persist(servico1);

        servico2 = new Servico();
        servico2.setNome("Exame");
        servico2.setDescricao("Exame de rotina");
        servico2.setDuracaoEmMinutos(15);
        servico2.setProfissional(profissional);
        entityManager.persist(servico2);

        entityManager.flush();
    }

    @Test
    void testFindAll() {
        List<Servico> resultado = servicoRepository.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void testFindById() {
        Optional<Servico> resultado = servicoRepository.findById(servico1.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Consulta Médica", resultado.get().getNome());
        assertEquals(30, resultado.get().getDuracaoEmMinutos());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Servico> resultado = servicoRepository.findById(999);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testSaveServico() {
        Servico novo = new Servico();
        novo.setNome("Terapia");
        novo.setDescricao("Sessão de terapia");
        novo.setDuracaoEmMinutos(60);
        novo.setProfissional(profissional);

        Servico salvo = servicoRepository.save(novo);

        assertNotNull(salvo.getId());
        assertEquals("Terapia", salvo.getNome());
        assertEquals(60, salvo.getDuracaoEmMinutos());
    }

    @Test
    void testUpdateServico() {
        servico1.setNome("Consulta Atualizada");
        servico1.setDuracaoEmMinutos(45);

        Servico atualizado = servicoRepository.save(servico1);

        assertEquals("Consulta Atualizada", atualizado.getNome());
        assertEquals(45, atualizado.getDuracaoEmMinutos());
    }

    @Test
    void testDeleteServico() {
        Integer id = servico1.getId();
        servicoRepository.delete(servico1);
        entityManager.flush();

        Optional<Servico> resultado = servicoRepository.findById(id);
        assertFalse(resultado.isPresent());
    }

    @Test
    void testServicoRelationWithProfissional() {
        Optional<Servico> resultado = servicoRepository.findById(servico1.getId());

        assertTrue(resultado.isPresent());
        assertNotNull(resultado.get().getProfissional());
        assertEquals("Dr. João", resultado.get().getProfissional().getNome());
    }
}
