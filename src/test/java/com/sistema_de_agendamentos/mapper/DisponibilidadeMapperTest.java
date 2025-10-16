package com.sistema_de_agendamentos.mapper;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeAgendarDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class DisponibilidadeMapperTest {

    private DisponibilidadeMapper disponibilidadeMapper;

    @BeforeEach
    void setUp() {
        disponibilidadeMapper = new DisponibilidadeMapper();
    }

    @Test
    void testToListagemDTO() {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setId(1);
        disponibilidade.setHoraInicio(LocalDateTime.of(2025, 10, 20, 9, 30));
        disponibilidade.setHoraFim(LocalDateTime.of(2025, 10, 20, 10, 30));

        DisponibilidadeListagemDTO dto = disponibilidadeMapper.toListagemDTO(disponibilidade);

        assertNotNull(dto);
        assertEquals(1, dto.id());
        assertEquals("2025-10-20", dto.data());
        assertEquals("09:30", dto.horaInicio());
        assertEquals("10:30", dto.horaFim());
    }

    @Test
    void testToListagemDTOWithMidnight() {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setId(2);
        disponibilidade.setHoraInicio(LocalDateTime.of(2025, 12, 25, 0, 0));
        disponibilidade.setHoraFim(LocalDateTime.of(2025, 12, 25, 1, 0));

        DisponibilidadeListagemDTO dto = disponibilidadeMapper.toListagemDTO(disponibilidade);

        assertNotNull(dto);
        assertEquals(2, dto.id());
        assertEquals("2025-12-25", dto.data());
        assertEquals("00:00", dto.horaInicio());
        assertEquals("01:00", dto.horaFim());
    }

    @Test
    void testToAgendarDTO() {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setId(3);
        disponibilidade.setHoraInicio(LocalDateTime.of(2025, 10, 20, 14, 0));
        disponibilidade.setHoraFim(LocalDateTime.of(2025, 10, 20, 15, 0));

        DisponibilidadeAgendarDTO dto = disponibilidadeMapper.toAgendarDTO(disponibilidade);

        assertNotNull(dto);
        assertEquals(3, dto.disponibilidadeId());
        assertEquals("20/10/2025 - 14:00 às 15:00", dto.descricaoDate());
    }

    @Test
    void testToAgendarDTOWithDifferentTime() {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setId(4);
        disponibilidade.setHoraInicio(LocalDateTime.of(2025, 6, 15, 8, 45));
        disponibilidade.setHoraFim(LocalDateTime.of(2025, 6, 15, 9, 45));

        DisponibilidadeAgendarDTO dto = disponibilidadeMapper.toAgendarDTO(disponibilidade);

        assertNotNull(dto);
        assertEquals(4, dto.disponibilidadeId());
        assertEquals("15/06/2025 - 08:45 às 09:45", dto.descricaoDate());
    }
}
