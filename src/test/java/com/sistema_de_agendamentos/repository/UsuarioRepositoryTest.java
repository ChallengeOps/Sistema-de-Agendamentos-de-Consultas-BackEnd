package com.sistema_de_agendamentos.repository;

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
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario cliente;
    private Usuario profissional;

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

        entityManager.flush();
    }

    @Test
    void testFindByEmail() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("cliente@test.com");

        assertTrue(resultado.isPresent());
        assertEquals("Cliente Teste", resultado.get().getNome());
        assertEquals(Usuario.ClienteTipo.CLIENTE, resultado.get().getAcesso());
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("naoexiste@test.com");

        assertFalse(resultado.isPresent());
    }

    @Test
    void testFindByAcessoCliente() {
        List<Usuario> resultado = usuarioRepository.findByAcesso(Usuario.ClienteTipo.CLIENTE);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cliente Teste", resultado.get(0).getNome());
    }

    @Test
    void testFindByAcessoProfissional() {
        List<Usuario> resultado = usuarioRepository.findByAcesso(Usuario.ClienteTipo.PROFISSIONAL);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Dr. João", resultado.get(0).getNome());
    }

    @Test
    void testSaveUsuario() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Novo Usuario");
        novoUsuario.setEmail("novo@test.com");
        novoUsuario.setPassword("senha789");
        novoUsuario.setAcesso(Usuario.ClienteTipo.CLIENTE);

        Usuario salvo = usuarioRepository.save(novoUsuario);

        assertNotNull(salvo.getId());
        assertEquals("Novo Usuario", salvo.getNome());

        Optional<Usuario> encontrado = usuarioRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
    }

    @Test
    void testFindAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
    }

    @Test
    void testDeleteUsuario() {
        usuarioRepository.delete(cliente);
        entityManager.flush();

        Optional<Usuario> resultado = usuarioRepository.findById(cliente.getId());
        assertFalse(resultado.isPresent());
    }

    @Test
    void testEmailUnique() {
        Usuario duplicado = new Usuario();
        duplicado.setNome("Duplicado");
        duplicado.setEmail("cliente@test.com"); // Email já existente
        duplicado.setPassword("senha");
        duplicado.setAcesso(Usuario.ClienteTipo.CLIENTE);

        assertThrows(Exception.class, () -> {
            entityManager.persist(duplicado);
            entityManager.flush();
        });
    }
}
