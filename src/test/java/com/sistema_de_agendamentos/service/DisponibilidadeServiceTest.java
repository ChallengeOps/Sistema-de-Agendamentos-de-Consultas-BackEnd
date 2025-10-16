package com.sistema_de_agendamentos.service;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeAgendarDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import com.sistema_de_agendamentos.entity.Servico;
import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.mapper.DisponibilidadeMapper;
import com.sistema_de_agendamentos.repository.DisponibilidadeRepository;
import com.sistema_de_agendamentos.utils.DateFormaterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
class DisponibilidadeServiceTest {

    @Mock
    private DisponibilidadeRepository disponibilidadeRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ServicoService servicoService;

    @Mock
    private DisponibilidadeMapper disponibilidadeMapper;

    @InjectMocks
    private DisponibilidadeService disponibilidadeService;

    private Usuario profissional;
    private Disponibilidade disponibilidade;

    @BeforeEach
    void setUp() {
        profissional = new Usuario();
        profissional.setId(1);
        profissional.setNome("Dr. João");

        disponibilidade = new Disponibilidade();
        disponibilidade.setId(1);
        disponibilidade.setHoraInicio(LocalDateTime.now().plusDays(1));
        disponibilidade.setHoraFim(LocalDateTime.now().plusDays(1).plusHours(1));
        disponibilidade.setProfissional(profissional);
    }

    @Test
    void testCriarDisponibilidadeSuccess() {
        DisponibilidadeDTO dto = new DisponibilidadeDTO("2025-10-20", "09:00", "10:00");
        LocalDateTime inicio = LocalDateTime.of(2025, 10, 20, 9, 0);
        LocalDateTime fim = LocalDateTime.of(2025, 10, 20, 10, 0);

        DateFormaterUtils.DatasDisponibilidade datas = new DateFormaterUtils.DatasDisponibilidade(inicio, fim);

        try (MockedStatic<DateFormaterUtils> mockedStatic = mockStatic(DateFormaterUtils.class)) {
            mockedStatic.when(() -> DateFormaterUtils.extrairDatas(dto)).thenReturn(datas);

            when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
            when(disponibilidadeRepository.save(any(Disponibilidade.class))).thenAnswer(i -> i.getArguments()[0]);

            disponibilidadeService.criarDisponibilidade(dto);

            verify(disponibilidadeRepository).save(any(Disponibilidade.class));
        }
    }

    @Test
    void testCriarDisponibilidadeHorarioNoPassado() {
        DisponibilidadeDTO dto = new DisponibilidadeDTO("2020-10-20", "09:00", "10:00");
        LocalDateTime inicio = LocalDateTime.of(2020, 10, 20, 9, 0);
        LocalDateTime fim = LocalDateTime.of(2020, 10, 20, 10, 0);

        DateFormaterUtils.DatasDisponibilidade datas = new DateFormaterUtils.DatasDisponibilidade(inicio, fim);

        try (MockedStatic<DateFormaterUtils> mockedStatic = mockStatic(DateFormaterUtils.class)) {
            mockedStatic.when(() -> DateFormaterUtils.extrairDatas(dto)).thenReturn(datas);

            when(usuarioService.getAuthenticationUser()).thenReturn(profissional);

            assertThrows(ResponseStatusException.class, () -> disponibilidadeService.criarDisponibilidade(dto));
            verify(disponibilidadeRepository, never()).save(any());
        }
    }

    @Test
    void testCriarDisponibilidadeHoraFimAntesDeInicio() {
        DisponibilidadeDTO dto = new DisponibilidadeDTO("2025-10-20", "10:00", "09:00");
        LocalDateTime inicio = LocalDateTime.of(2025, 10, 20, 10, 0);
        LocalDateTime fim = LocalDateTime.of(2025, 10, 20, 9, 0);

        DateFormaterUtils.DatasDisponibilidade datas = new DateFormaterUtils.DatasDisponibilidade(inicio, fim);

        try (MockedStatic<DateFormaterUtils> mockedStatic = mockStatic(DateFormaterUtils.class)) {
            mockedStatic.when(() -> DateFormaterUtils.extrairDatas(dto)).thenReturn(datas);

            when(usuarioService.getAuthenticationUser()).thenReturn(profissional);

            assertThrows(ResponseStatusException.class, () -> disponibilidadeService.criarDisponibilidade(dto));
            verify(disponibilidadeRepository, never()).save(any());
        }
    }

    @Test
    void testBuscarPorServico() {
        Servico servico = new Servico();
        servico.setId(1);
        servico.setProfissional(profissional);

        List<Disponibilidade> disponibilidades = List.of(disponibilidade);
        DisponibilidadeAgendarDTO dto = new DisponibilidadeAgendarDTO(1, "20/10/2025 - 09:00 às 10:00");

        when(servicoService.findEntity(1)).thenReturn(servico);
        when(disponibilidadeRepository.findByProfissional(profissional)).thenReturn(disponibilidades);
        when(disponibilidadeMapper.toAgendarDTO(disponibilidade)).thenReturn(dto);

        List<DisponibilidadeAgendarDTO> resultado = disponibilidadeService.buscarPorServico(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testListarPorProfissional() {
        List<Disponibilidade> disponibilidades = List.of(disponibilidade);
        DisponibilidadeListagemDTO dto = new DisponibilidadeListagemDTO(1, "2025-10-20", "09:00", "10:00");

        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
        when(disponibilidadeRepository.findByProfissional(profissional)).thenReturn(disponibilidades);
        when(disponibilidadeMapper.toListagemDTO(disponibilidade)).thenReturn(dto);

        List<DisponibilidadeListagemDTO> resultado = disponibilidadeService.listarPorProfissional();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testDelete() {
        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
        when(disponibilidadeRepository.findById(1)).thenReturn(Optional.of(disponibilidade));

        disponibilidadeService.delete(1);

        verify(disponibilidadeRepository).delete(disponibilidade);
    }

    @Test
    void testDeleteSemPermissao() {
        Usuario outroProfissional = new Usuario();
        outroProfissional.setId(2);

        when(usuarioService.getAuthenticationUser()).thenReturn(outroProfissional);
        when(disponibilidadeRepository.findById(1)).thenReturn(Optional.of(disponibilidade));

        assertThrows(ResponseStatusException.class, () -> disponibilidadeService.delete(1));
        verify(disponibilidadeRepository, never()).delete(any());
    }

    @Test
    void testFindEntitySuccess() {
        when(disponibilidadeRepository.findById(1)).thenReturn(Optional.of(disponibilidade));

        Disponibilidade resultado = disponibilidadeService.findEntity(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void testFindEntityNotFound() {
        when(disponibilidadeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> disponibilidadeService.findEntity(1));
    }

    @Test
    void testFindEntityPermissionSuccess() {
        when(usuarioService.getAuthenticationUser()).thenReturn(profissional);
        when(disponibilidadeRepository.findById(1)).thenReturn(Optional.of(disponibilidade));

        Disponibilidade resultado = disponibilidadeService.findEntityPermission(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void testFindEntityPermissionForbidden() {
        Usuario outroProfissional = new Usuario();
        outroProfissional.setId(2);

        when(usuarioService.getAuthenticationUser()).thenReturn(outroProfissional);
        when(disponibilidadeRepository.findById(1)).thenReturn(Optional.of(disponibilidade));

        assertThrows(ResponseStatusException.class, () -> disponibilidadeService.findEntityPermission(1));
    }
}
