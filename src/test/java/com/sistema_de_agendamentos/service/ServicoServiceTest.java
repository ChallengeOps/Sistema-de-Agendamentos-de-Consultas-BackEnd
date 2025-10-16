package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoListagemDTO;
import com.sistema_de_agendamentos.entity.Servico;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.mapper.ServicoMapper;
import com.sistema_de_agendamentos.repository.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ServicoMapper servicoMapper;

    @InjectMocks
    private ServicoService servicoService;

    private Usuario profissional;
    private Servico servico;

    @BeforeEach
    void setUp() {
        profissional = new Usuario();
        profissional.setId(1);
        profissional.setNome("Dr. João");
        profissional.setAcesso(Usuario.ClienteTipo.PROFISSIONAL);

        servico = new Servico();
        servico.setId(1);
        servico.setNome("Consulta");
        servico.setDescricao("Consulta geral");
        servico.setDuracaoEmMinutos(30);
        servico.setProfissional(profissional);
    }

    @Test
    void testCadastrarServico() {
        ServicoDTO dto = new ServicoDTO("Consulta", "Consulta geral", 30);
        ServicoListagemDTO listagemDTO = new ServicoListagemDTO(1, "Consulta", "Consulta geral", 30, "Dr. João");

        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
        when(servicoMapper.fromDTO(dto, profissional)).thenReturn(servico);
        when(servicoRepository.save(servico)).thenReturn(servico);
        when(servicoMapper.toListagemDTO(servico)).thenReturn(listagemDTO);

        ServicoListagemDTO resultado = servicoService.cadastrarServico(dto);

        assertNotNull(resultado);
        assertEquals("Consulta", resultado.nome());
        verify(servicoRepository).save(servico);
    }

    @Test
    void testListarServicosPorProfissional() {
        List<Servico> servicos = new ArrayList<>();
        servicos.add(servico);
        profissional.setServicos(servicos);

        ServicoListagemDTO listagemDTO = new ServicoListagemDTO(1, "Consulta", "Consulta geral", 30, "Dr. João");

        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
        when(servicoMapper.toListagemDTO(servico)).thenReturn(listagemDTO);

        List<ServicoListagemDTO> resultado = servicoService.listarServicosPorProfissional();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Consulta", resultado.get(0).nome());
    }

    @Test
    void testListarServicosPorProfissionalSemServicos() {
        profissional.setServicos(new ArrayList<>());

        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);

        assertThrows(ResponseStatusException.class, () -> servicoService.listarServicosPorProfissional());
    }

    @Test
    void testDeletarServico() {
        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
        when(servicoRepository.findById(1)).thenReturn(Optional.of(servico));

        servicoService.deletarServico(1);

        verify(servicoRepository).delete(servico);
    }

    @Test
    void testDeletarServicoSemPermissao() {
        Usuario outroProfissional = new Usuario();
        outroProfissional.setId(2);

        when(usuarioService.getAuthenticationUser()).thenReturn(outroProfissional);
        when(servicoRepository.findById(1)).thenReturn(Optional.of(servico));

        assertThrows(ResponseStatusException.class, () -> servicoService.deletarServico(1));
        verify(servicoRepository, never()).delete(any());
    }

    @Test
    void testUpdate() {
        ServicoListagemDTO dto = new ServicoListagemDTO(1, "Consulta Atualizada", "Nova descrição", 45, "Dr. João");

        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
        when(servicoRepository.findById(1)).thenReturn(Optional.of(servico));
        when(servicoRepository.save(servico)).thenReturn(servico);
        when(servicoMapper.toListagemDTO(servico)).thenReturn(dto);

        ServicoListagemDTO resultado = servicoService.update(1, dto);

        assertNotNull(resultado);
        assertEquals("Consulta Atualizada", resultado.nome());
        verify(servicoMapper).updateFromListagemDTO(servico, dto);
        verify(servicoRepository).save(servico);
    }

    @Test
    void testListarServicos() {
        List<Servico> servicos = List.of(servico);
        ServicoListagemDTO listagemDTO = new ServicoListagemDTO(1, "Consulta", "Consulta geral", 30, "Dr. João");

        when(servicoRepository.findAll()).thenReturn(servicos);
        when(servicoMapper.toListagemDTO(servico)).thenReturn(listagemDTO);

        List<ServicoListagemDTO> resultado = servicoService.listarServicos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testListarServicosSemServicos() {
        when(servicoRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> servicoService.listarServicos());
    }

    @Test
    void testFindEntitySuccess() {
        when(servicoRepository.findById(1)).thenReturn(Optional.of(servico));

        Servico resultado = servicoService.findEntity(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void testFindEntityNotFound() {
        when(servicoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> servicoService.findEntity(1));
    }

    @Test
    void testFindEntityWithPermissionSuccess() {
        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
        when(servicoRepository.findById(1)).thenReturn(Optional.of(servico));

        Servico resultado = servicoService.findEntityWithPermission(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void testFindEntityWithPermissionForbidden() {
        Usuario outroProfissional = new Usuario();
        outroProfissional.setId(2);

        when(usuarioService.getAuthenticationUser()).thenReturn(outroProfissional);
        when(servicoRepository.findById(1)).thenReturn(Optional.of(servico));

        assertThrows(ResponseStatusException.class, () -> servicoService.findEntityWithPermission(1));
    }
}
