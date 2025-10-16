package com.sistema_de_agendamentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema_de_agendamentos.config.security.TokenService;
import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoCreateDTO;
import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoDTO;
import com.sistema_de_agendamentos.repository.UsuarioRepository;
import com.sistema_de_agendamentos.service.AgendamentoService;
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

@WebMvcTest(AgendamentoController.class)
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgendamentoService agendamentoService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    @WithMockUser
    void testCriarAgendamento() throws Exception {
        AgendamentoCreateDTO dto = new AgendamentoCreateDTO(1, 1);

        doNothing().when(agendamentoService).criarAgendamento(any(AgendamentoCreateDTO.class));

        mockMvc.perform(post("/agendamentos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(content().string("Agendamento criado com sucesso"));

        verify(agendamentoService).criarAgendamento(any(AgendamentoCreateDTO.class));
    }

    @Test
    @WithMockUser
    void testListarAgendamentos() throws Exception {
        AgendamentoDTO dto1 = new AgendamentoDTO(1, "Cliente", "Consulta", "20/10/2025 - 09:00 às 10:00");
        AgendamentoDTO dto2 = new AgendamentoDTO(2, "Cliente", "Exame", "21/10/2025 - 14:00 às 15:00");
        List<AgendamentoDTO> agendamentos = List.of(dto1, dto2);

        when(agendamentoService.listarAgendamentosParaUsuarioAtual()).thenReturn(agendamentos);

        mockMvc.perform(get("/agendamentos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nomeProfissional").value("Cliente"))
            .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @WithMockUser
    void testDeleteAgendamento() throws Exception {
        doNothing().when(agendamentoService).delete(1);

        mockMvc.perform(delete("/agendamentos/1")
                .with(csrf()))
            .andExpect(status().isNoContent());

        verify(agendamentoService).delete(1);
    }
}
