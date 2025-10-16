package com.sistema_de_agendamentos.mapper;

import com.sistema_de_agendamentos.config.security.dto.RegisterRequestDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioRegisterDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    private UsuarioMapper usuarioMapper;

    @BeforeEach
    void setUp() {
        usuarioMapper = new UsuarioMapper();
    }

    @Test
    void testFromRegisterRequestDTO() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
            "João Silva",
            "joao@example.com",
            "senha123"
        );

        Usuario usuario = usuarioMapper.fromRegisterRequestDTO(dto);

        assertNotNull(usuario);
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@example.com", usuario.getEmail());
        assertEquals("senha123", usuario.getPassword());
        assertNull(usuario.getId());
        assertNull(usuario.getAcesso());
        assertNull(usuario.getAgendamentos());
        assertNull(usuario.getServicos());
        assertNull(usuario.getDisponibilidades());
    }

    @Test
    void testFromRegisterRequestDTONull() {
        Usuario usuario = usuarioMapper.fromRegisterRequestDTO(null);
        assertNull(usuario);
    }

    @Test
    void testFromRegisterDTO() {
        UsuarioRegisterDTO dto = new UsuarioRegisterDTO(
            "Maria Santos",
            "maria@example.com",
            "senha456",
            Usuario.ClienteTipo.CLIENTE
        );

        Usuario usuario = usuarioMapper.fromRegisterDTO(dto);

        assertNotNull(usuario);
        assertEquals("Maria Santos", usuario.getNome());
        assertEquals("maria@example.com", usuario.getEmail());
        assertEquals("senha456", usuario.getPassword());
        assertEquals(Usuario.ClienteTipo.CLIENTE, usuario.getAcesso());
        assertNull(usuario.getId());
        assertNull(usuario.getAgendamentos());
        assertNull(usuario.getServicos());
        assertNull(usuario.getDisponibilidades());
    }

    @Test
    void testFromRegisterDTONull() {
        Usuario usuario = usuarioMapper.fromRegisterDTO(null);
        assertNull(usuario);
    }

    @Test
    void testUpdateFromDTO() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Nome Original");
        usuario.setEmail("original@example.com");
        usuario.setAcesso(Usuario.ClienteTipo.CLIENTE);

        UsuarioDTO dto = new UsuarioDTO(
            "Nome Atualizado",
            "atualizado@example.com",
            Usuario.ClienteTipo.PROFISSIONAL
        );

        Usuario resultado = usuarioMapper.updateFromDTO(dto, usuario);

        assertNotNull(resultado);
        assertEquals("Nome Atualizado", resultado.getNome());
        assertEquals("atualizado@example.com", resultado.getEmail());
        assertEquals(Usuario.ClienteTipo.PROFISSIONAL, resultado.getAcesso());
        assertEquals(1, resultado.getId()); // ID não deve mudar
    }

    @Test
    void testUpdateFromDTOWithNullFields() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Nome Original");
        usuario.setEmail("original@example.com");
        usuario.setAcesso(Usuario.ClienteTipo.CLIENTE);

        UsuarioDTO dto = new UsuarioDTO(null, null, null);

        Usuario resultado = usuarioMapper.updateFromDTO(dto, usuario);

        // Os valores originais devem ser mantidos
        assertEquals("Nome Original", resultado.getNome());
        assertEquals("original@example.com", resultado.getEmail());
        assertEquals(Usuario.ClienteTipo.CLIENTE, resultado.getAcesso());
    }

    @Test
    void testUpdateFromDTONull() {
        Usuario usuario = new Usuario();
        usuario.setNome("Nome");

        Usuario resultado = usuarioMapper.updateFromDTO(null, usuario);

        assertEquals(usuario, resultado);
    }

    @Test
    void testUpdateFromDTOWithNullUsuario() {
        UsuarioDTO dto = new UsuarioDTO("Nome", "email@test.com", Usuario.ClienteTipo.CLIENTE);

        Usuario resultado = usuarioMapper.updateFromDTO(dto, null);

        assertNull(resultado);
    }
}
