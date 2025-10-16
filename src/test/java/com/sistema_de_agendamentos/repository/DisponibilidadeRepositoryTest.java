package com.sistema_de_agendamentos.repository;

import com.sistema_de_agendamentos.entity.Disponibilidade;
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
class DisponibilidadeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    private Usuario profissional1;
    private Usuario profissional2;
    private Disponibilidade disponibilidade1;
    private Disponibilidade disponibilidade2;

    @BeforeEach
    void setUp() {
        profissional1 = new Usuario();
        profissional1.setNome("Dr. João");
        profissional1.setEmail("joao@test.com");
        profissional1.setPassword("senha123");
        profissional1.setAcesso(Usuario.ClienteTipo.PROFISSIONAL);
        entityManager.persist(profissional1);

        profissional2 = new Usuario();
        profissional2.setNome("Dr. Maria");
        profissional2.setEmail("maria@test.com");
        profissional2.setPassword("senha456");
        profissional2.setAcesso(Usuario.ClienteTipo.PROFISSIONAL);
        entityManager.persist(profissional2);

        disponibilidade1 = new Disponibilidade();
        disponibilidade1.setHoraInicio(LocalDateTime.of(2025, 10, 20, 9, 0));
        disponibilidade1.setHoraFim(LocalDateTime.of(2025, 10, 20, 10, 0));
        disponibilidade1.setProfissional(profissional1);
        entityManager.persist(disponibilidade1);

        disponibilidade2 = new Disponibilidade();
        disponibilidade2.setHoraInicio(LocalDateTime.of(2025, 10, 20, 14, 0));
        disponibilidade2.setHoraFim(LocalDateTime.of(2025, 10, 20, 15, 0));
        disponibilidade2.setProfissional(profissional1);
        entityManager.persist(disponibilidade2);

        entityManager.flush();
    }

    @Test
    void testFindByProfissional() {
        List<Disponibilidade> resultado = disponibilidadeRepository.findByProfissional(profissional1);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(d -> d.getProfissional().equals(profissional1)));
    }

    @Test
    void testFindByProfissionalSemDisponibilidades() {
        List<Disponibilidade> resultado = disponibilidadeRepository.findByProfissional(profissional2);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testSaveDisponibilidade() {
        Disponibilidade nova = new Disponibilidade();
        nova.setHoraInicio(LocalDateTime.of(2025, 10, 21, 10, 0));
        nova.setHoraFim(LocalDateTime.of(2025, 10, 21, 11, 0));
        nova.setProfissional(profissional2);

        Disponibilidade salva = disponibilidadeRepository.save(nova);

        assertNotNull(salva.getId());
        assertEquals(profissional2, salva.getProfissional());
    }

    @Test
    void testFindById() {
        Optional<Disponibilidade> resultado = disponibilidadeRepository.findById(disponibilidade1.getId());

        assertTrue(resultado.isPresent());
        assertEquals(LocalDateTime.of(2025, 10, 20, 9, 0), resultado.get().getHoraInicio());
    }

    @Test
    void testDeleteDisponibilidade() {
        disponibilidadeRepository.delete(disponibilidade1);
        entityManager.flush();

        Optional<Disponibilidade> resultado = disponibilidadeRepository.findById(disponibilidade1.getId());
        assertFalse(resultado.isPresent());
    }

    @Test
    void testFindAll() {
        List<Disponibilidade> resultado = disponibilidadeRepository.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }
}
