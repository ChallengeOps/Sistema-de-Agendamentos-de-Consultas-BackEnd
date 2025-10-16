package com.sistema_de_agendamentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema_de_agendamentos.config.security.TokenService;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeAgendarDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.repository.UsuarioRepository;
import com.sistema_de_agendamentos.service.DisponibilidadeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DisponibilidadeController.class)
class DisponibilidadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DisponibilidadeService disponibilidadeService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    @WithMockUser
    void testCriarDisponibilidade() throws Exception {
        DisponibilidadeDTO dto = new DisponibilidadeDTO("2025-10-20", "09:00", "10:00");

        doNothing().when(disponibilidadeService).criarDisponibilidade(any(DisponibilidadeDTO.class));

        mockMvc.perform(post("/disponibilidades")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());

        verify(disponibilidadeService).criarDisponibilidade(any(DisponibilidadeDTO.class));
    }

    @Test
    @WithMockUser
    void testListarPorProfissional() throws Exception {
        DisponibilidadeListagemDTO dto1 = new DisponibilidadeListagemDTO(1, "2025-10-20", "09:00", "10:00");
        DisponibilidadeListagemDTO dto2 = new DisponibilidadeListagemDTO(2, "2025-10-21", "14:00", "15:00");
        List<DisponibilidadeListagemDTO> disponibilidades = List.of(dto1, dto2);

        when(disponibilidadeService.listarPorProfissional()).thenReturn(disponibilidades);

        mockMvc.perform(get("/disponibilidades/profissional"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].data").value("2025-10-20"))
            .andExpect(jsonPath("$[0].horaInicio").value("09:00"))
            .andExpect(jsonPath("$[1].data").value("2025-10-21"));
    }

    @Test
    @WithMockUser
    void testBuscarPorServico() throws Exception {
        DisponibilidadeAgendarDTO dto1 = new DisponibilidadeAgendarDTO(1, "20/10/2025 - 09:00 às 10:00");
        DisponibilidadeAgendarDTO dto2 = new DisponibilidadeAgendarDTO(2, "21/10/2025 - 14:00 às 15:00");
        List<DisponibilidadeAgendarDTO> disponibilidades = List.of(dto1, dto2);

        when(disponibilidadeService.buscarPorServico(1)).thenReturn(disponibilidades);

        mockMvc.perform(get("/disponibilidades/servico/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].disponibilidadeId").value(1))
            .andExpect(jsonPath("$[0].descricaoDate").value("20/10/2025 - 09:00 às 10:00"));
    }

    @Test
    @WithMockUser
    void testDeleteDisponibilidade() throws Exception {
        doNothing().when(disponibilidadeService).delete(1);

        mockMvc.perform(delete("/disponibilidades/1")
                .with(csrf()))
            .andExpect(status().isNoContent());

        verify(disponibilidadeService).delete(1);
    }
}
