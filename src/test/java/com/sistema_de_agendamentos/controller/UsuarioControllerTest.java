package com.sistema_de_agendamentos.controller;

import com.sistema_de_agendamentos.config.security.TokenService;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioPerfilDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.UsuarioRepository;
import com.sistema_de_agendamentos.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    @WithMockUser
    void testGetPerfil() throws Exception {
        UsuarioPerfilDTO perfilDTO = new UsuarioPerfilDTO(
            1,
            "João Silva",
            "joao@example.com",
            Usuario.ClienteTipo.CLIENTE
        );

        when(usuarioService.me()).thenReturn(perfilDTO);

        mockMvc.perform(get("/usuario/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nome").value("João Silva"))
            .andExpect(jsonPath("$.email").value("joao@example.com"))
            .andExpect(jsonPath("$.acesso").value("CLIENTE"));
    }
}
