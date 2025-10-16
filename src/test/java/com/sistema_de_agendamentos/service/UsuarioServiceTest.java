package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioPerfilDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioRegisterDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.mapper.UsuarioMapper;
import com.sistema_de_agendamentos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateUsuarioSuccess() {
        UsuarioRegisterDTO dto = new UsuarioRegisterDTO(
            "João Silva",
            "joao@example.com",
            "senha123",
            Usuario.ClienteTipo.CLIENTE
        );

        Usuario usuario = new Usuario();
        usuario.setEmail("joao@example.com");
        usuario.setNome("João Silva");

        when(usuarioMapper.fromRegisterDTO(dto)).thenReturn(usuario);
        when(usuarioRepository.findByEmail("joao@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.create(dto);

        assertNotNull(resultado);
        assertEquals("joao@example.com", resultado.getEmail());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testCreateUsuarioEmailJaCadastrado() {
        UsuarioRegisterDTO dto = new UsuarioRegisterDTO(
            "João Silva",
            "joao@example.com",
            "senha123",
            Usuario.ClienteTipo.CLIENTE
        );

        Usuario usuario = new Usuario();
        usuario.setEmail("joao@example.com");

        when(usuarioMapper.fromRegisterDTO(dto)).thenReturn(usuario);
        when(usuarioRepository.findByEmail("joao@example.com")).thenReturn(Optional.of(usuario));

        assertThrows(ResponseStatusException.class, () -> usuarioService.create(dto));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testMe() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@example.com");
        usuario.setAcesso(Usuario.ClienteTipo.CLIENTE);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("joao@example.com");
        when(usuarioRepository.findByEmail("joao@example.com")).thenReturn(Optional.of(usuario));

        UsuarioPerfilDTO resultado = usuarioService.me();

        assertNotNull(resultado);
        assertEquals(1, resultado.id());
        assertEquals("João Silva", resultado.nome());
        assertEquals("joao@example.com", resultado.email());
        assertEquals(Usuario.ClienteTipo.CLIENTE, resultado.acesso());
    }

    @Test
    void testFindEntitySuccess() {
        Usuario usuario = new Usuario();
        usuario.setId(1);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findEntity(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void testFindEntityNotFound() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> usuarioService.findEntity(1));
    }

    @Test
    void testFindByEmailSuccess() {
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@example.com");

        when(usuarioRepository.findByEmail("joao@example.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findByEmail("joao@example.com");

        assertNotNull(resultado);
        assertEquals("joao@example.com", resultado.getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        when(usuarioRepository.findByEmail("joao@example.com")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> usuarioService.findByEmail("joao@example.com"));
    }

    @Test
    void testGetAuthenticationUser() {
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("joao@example.com");
        when(usuarioRepository.findByEmail("joao@example.com")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.getAuthenticationUser();

        assertNotNull(resultado);
        assertEquals("joao@example.com", resultado.getEmail());
    }
}
