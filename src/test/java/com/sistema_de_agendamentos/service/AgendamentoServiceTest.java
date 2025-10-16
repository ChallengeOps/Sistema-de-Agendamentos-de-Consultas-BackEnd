package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoCreateDTO;
import com.sistema_de_agendamentos.controller.dto.agendamento.AgendamentoDTO;
import com.sistema_de_agendamentos.entity.Agendamento;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.entity.Servico;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.AgendamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private DisponibilidadeService disponibilidadeService;

    @Mock
    private ServicoService servicoService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private Usuario cliente;
    private Usuario profissional;
    private Servico servico;
    private Disponibilidade disponibilidade;
    private Agendamento agendamento;

    @BeforeEach
    void setUp() {
        cliente = new Usuario();
        cliente.setId(1);
        cliente.setNome("Cliente Teste");
        cliente.setAcesso(Usuario.ClienteTipo.CLIENTE);

        profissional = new Usuario();
        profissional.setId(2);
        profissional.setNome("Dr. João");
        profissional.setAcesso(Usuario.ClienteTipo.PROFISSIONAL);

        servico = new Servico();
        servico.setId(1);
        servico.setNome("Consulta");
        servico.setProfissional(profissional);

        disponibilidade = new Disponibilidade();
        disponibilidade.setId(1);
        disponibilidade.setProfissional(profissional);
        disponibilidade.setHoraInicio(LocalDateTime.now().plusDays(1));
        disponibilidade.setHoraFim(LocalDateTime.now().plusDays(1).plusHours(1));

        agendamento = new Agendamento();
        agendamento.setId(1);
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setDisponibilidade(disponibilidade);
        agendamento.setStatus(Agendamento.Status.PENDENTE);
    }

    @Test
    void testCriarAgendamentoSuccess() {
        AgendamentoCreateDTO dto = new AgendamentoCreateDTO(1, 1);

        when(usuarioService.getAuthenticationUser()).thenReturn(cliente);
        when(servicoService.findEntity(1)).thenReturn(servico);
        when(usuarioService.findEntity(2)).thenReturn(profissional);
        when(disponibilidadeService.findEntity(1)).thenReturn(disponibilidade);
        when(agendamentoRepository.save(any(Agendamento.class))).thenAnswer(i -> i.getArguments()[0]);

        agendamentoService.criarAgendamento(dto);

        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    void testCriarAgendamentoProfissionalDiferente() {
        Usuario outroProfissional = new Usuario();
        outroProfissional.setId(3);

        disponibilidade.setProfissional(outroProfissional);

        AgendamentoCreateDTO dto = new AgendamentoCreateDTO(1, 1);

        when(usuarioService.getAuthenticationUser()).thenReturn(cliente);
        when(servicoService.findEntity(1)).thenReturn(servico);
        when(disponibilidadeService.findEntity(1)).thenReturn(disponibilidade);

        assertThrows(ResponseStatusException.class, () -> agendamentoService.criarAgendamento(dto));
        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    void testListarAgendamentosParaCliente() {
        List<Agendamento> agendamentos = List.of(agendamento);
        cliente.setAgendamentos(agendamentos);

        when(usuarioService.getAuthenticationUser()).thenReturn(cliente);

        List<AgendamentoDTO> resultado = agendamentoService.listarAgendamentosParaUsuarioAtual();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cliente Teste", resultado.get(0).nomeProfissional());
    }

    @Test
    void testListarAgendamentosParaProfissional() {
        List<Servico> servicos = new ArrayList<>();
        servico.setAgendamentos(List.of(agendamento));
        servicos.add(servico);
        profissional.setServicos(servicos);

        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);

        List<AgendamentoDTO> resultado = agendamentoService.listarAgendamentosParaUsuarioAtual();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testListarAgendamentosSemAgendamentos() {
        cliente.setAgendamentos(new ArrayList<>());

        when(usuarioService.getAuthenticationUser()).thenReturn(cliente);

        assertThrows(ResponseStatusException.class, () -> agendamentoService.listarAgendamentosParaUsuarioAtual());
    }

    @Test
    void testDelete() {
        when(usuarioService.getAuthenticationUser()).thenReturn(cliente);
        when(agendamentoRepository.findById(1)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any(Agendamento.class))).thenAnswer(i -> i.getArguments()[0]);

        agendamentoService.delete(1);

        verify(agendamentoRepository).save(any(Agendamento.class));
        verify(agendamentoRepository).deleteById(1);
    }

    @Test
    void testDeleteSemPermissao() {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(3);

        when(usuarioService.getAuthenticationUser()).thenReturn(outroUsuario);
        when(agendamentoRepository.findById(1)).thenReturn(Optional.of(agendamento));

        assertThrows(ResponseStatusException.class, () -> agendamentoService.delete(1));
        verify(agendamentoRepository, never()).deleteById(any());
    }

    @Test
    void testFindEntityPermissionCliente() {
        when(usuarioService.getAuthenticationUser()).thenReturn(cliente);
        when(agendamentoRepository.findById(1)).thenReturn(Optional.of(agendamento));

        Agendamento resultado = agendamentoService.findEntityPermission(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void testFindEntityPermissionProfissional() {
        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
        when(agendamentoRepository.findById(1)).thenReturn(Optional.of(agendamento));

        Agendamento resultado = agendamentoService.findEntityPermission(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void testFindEntityPermissionForbidden() {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(3);

        when(usuarioService.getAuthenticationUser()).thenReturn(outroUsuario);
        when(agendamentoRepository.findById(1)).thenReturn(Optional.of(agendamento));

        assertThrows(ResponseStatusException.class, () -> agendamentoService.findEntityPermission(1));
    }

    @Test
    void testFindEntityPermissionNotFound() {
        when(agendamentoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> agendamentoService.findEntityPermission(1));
    }
}
