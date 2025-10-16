package com.sistema_de_agendamentos.mapper;

import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.controller.dto.servico.ServicoListagemDTO;
import com.sistema_de_agendamentos.entity.Servico;
import com.sistema_de_agendamentos.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServicoMapperTest {

    private ServicoMapper servicoMapper;

    @BeforeEach
    void setUp() {
        servicoMapper = new ServicoMapper();
    }

    @Test
    void testFromDTO() {
        Usuario profissional = new Usuario();
        profissional.setId(1);
        profissional.setNome("Dr. João");

        ServicoDTO dto = new ServicoDTO(
            "Consulta Médica",
            "Consulta geral",
            30
        );

        Servico servico = servicoMapper.fromDTO(dto, profissional);

        assertNotNull(servico);
        assertEquals("Consulta Médica", servico.getNome());
        assertEquals("Consulta geral", servico.getDescricao());
        assertEquals(30, servico.getDuracaoEmMinutos());
        assertEquals(profissional, servico.getProfissional());
        assertNull(servico.getAgendamentos());
    }

    @Test
    void testFromDTONullDTO() {
        Usuario profissional = new Usuario();
        Servico servico = servicoMapper.fromDTO(null, profissional);
        assertNull(servico);
    }

    @Test
    void testFromDTONullProfissional() {
        ServicoDTO dto = new ServicoDTO("Nome", "Descricao", 30);
        Servico servico = servicoMapper.fromDTO(dto, null);
        assertNull(servico);
    }

    @Test
    void testUpdateFromDTO() {
        Servico servico = new Servico();
        servico.setId(1);
        servico.setNome("Nome Original");
        servico.setDescricao("Descricao Original");
        servico.setDuracaoEmMinutos(30);

        ServicoDTO dto = new ServicoDTO(
            "Nome Atualizado",
            "Descricao Atualizada",
            60
        );

        servicoMapper.updateFromDTO(dto, servico);

        assertEquals("Nome Atualizado", servico.getNome());
        assertEquals("Descricao Atualizada", servico.getDescricao());
        assertEquals(60, servico.getDuracaoEmMinutos());
    }

    @Test
    void testUpdateFromDTOWithNullFields() {
        Servico servico = new Servico();
        servico.setNome("Nome Original");
        servico.setDescricao("Descricao Original");
        servico.setDuracaoEmMinutos(30);

        ServicoDTO dto = new ServicoDTO(null, null, null);

        servicoMapper.updateFromDTO(dto, servico);

        // Valores originais devem ser mantidos quando campos são null
        assertEquals("Nome Original", servico.getNome());
        assertEquals("Descricao Original", servico.getDescricao());
        assertEquals(30, servico.getDuracaoEmMinutos());
    }

    @Test
    void testUpdateFromDTONull() {
        Servico servico = new Servico();
        servico.setNome("Nome");

        servicoMapper.updateFromDTO(null, servico);

        assertEquals("Nome", servico.getNome());
    }

    @Test
    void testToListagemDTO() {
        Usuario profissional = new Usuario();
        profissional.setNome("Dr. Maria");

        Servico servico = new Servico();
        servico.setId(1);
        servico.setNome("Terapia");
        servico.setDescricao("Sessão de terapia");
        servico.setDuracaoEmMinutos(45);
        servico.setProfissional(profissional);

        ServicoListagemDTO dto = servicoMapper.toListagemDTO(servico);

        assertNotNull(dto);
        assertEquals(1, dto.id());
        assertEquals("Terapia", dto.nome());
        assertEquals("Sessão de terapia", dto.descricao());
        assertEquals(45, dto.duracaoEmMinutos());
        assertEquals("Dr. Maria", dto.nomeProfissional());
    }

    @Test
    void testUpdateFromListagemDTO() {
        Servico servico = new Servico();
        servico.setId(1);
        servico.setNome("Nome Original");

        Usuario profissional = new Usuario();
        profissional.setId(1);
        servico.setProfissional(profissional);

        ServicoListagemDTO dto = new ServicoListagemDTO(
            1,
            "Nome Atualizado",
            "Descricao Atualizada",
            90,
            "Outro Profissional"
        );

        servicoMapper.updateFromListagemDTO(servico, dto);

        assertEquals("Nome Atualizado", servico.getNome());
        assertEquals("Descricao Atualizada", servico.getDescricao());
        assertEquals(90, servico.getDuracaoEmMinutos());
        // O profissional não deve ser alterado
        assertEquals(profissional, servico.getProfissional());
    }
}
