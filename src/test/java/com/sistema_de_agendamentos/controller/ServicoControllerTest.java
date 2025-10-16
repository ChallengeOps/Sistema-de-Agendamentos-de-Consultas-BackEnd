package com.sistema_de_agendamentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema_de_agendamentos.config.security.TokenService;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoListagemDTO;
import com.sistema_de_agendamentos.repository.UsuarioRepository;
import com.sistema_de_agendamentos.service.ServicoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServicoController.class)
class ServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ServicoService servicoService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    @WithMockUser
    void testCriarServico() throws Exception {
        ServicoDTO dto = new ServicoDTO("Consulta", "Consulta geral", 30);
        ServicoListagemDTO listagemDTO = new ServicoListagemDTO(1, "Consulta", "Consulta geral", 30, "Dr. João");

        when(servicoService.cadastrarServico(any(ServicoDTO.class))).thenReturn(listagemDTO);

        mockMvc.perform(post("/servicos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nome").value("Consulta"))
            .andExpect(jsonPath("$.descricao").value("Consulta geral"))
            .andExpect(jsonPath("$.duracaoEmMinutos").value(30))
            .andExpect(jsonPath("$.nomeProfissional").value("Dr. João"));
    }

    @Test
    @WithMockUser
    void testListarTodos() throws Exception {
        ServicoListagemDTO dto1 = new ServicoListagemDTO(1, "Consulta", "Consulta geral", 30, "Dr. João");
        ServicoListagemDTO dto2 = new ServicoListagemDTO(2, "Exame", "Exame de rotina", 15, "Dr. Maria");
        List<ServicoListagemDTO> servicos = List.of(dto1, dto2);

        when(servicoService.listarServicos()).thenReturn(servicos);

        mockMvc.perform(get("/servicos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].nome").value("Consulta"))
            .andExpect(jsonPath("$[1].nome").value("Exame"));
    }

    @Test
    @WithMockUser
    void testDeletarServico() throws Exception {
        doNothing().when(servicoService).deletarServico(1);

        mockMvc.perform(delete("/servicos/1")
                .with(csrf()))
            .andExpect(status().isNoContent());

        verify(servicoService).deletarServico(1);
    }

    @Test
    @WithMockUser
    void testEditarServico() throws Exception {
        ServicoListagemDTO dto = new ServicoListagemDTO(1, "Consulta Atualizada", "Nova descrição", 45, "Dr. João");

        when(servicoService.update(eq(1), any(ServicoListagemDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/servicos/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Consulta Atualizada"))
            .andExpect(jsonPath("$.duracaoEmMinutos").value(45));
    }
}
