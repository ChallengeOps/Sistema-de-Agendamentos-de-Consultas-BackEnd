package com.sistema_de_agendamentos.utils;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class DateFormaterUtilsTest {

    @Test
    void testDateFormate() {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setHoraInicio(LocalDateTime.of(2025, 10, 20, 9, 30));
        disponibilidade.setHoraFim(LocalDateTime.of(2025, 10, 20, 10, 30));

        String resultado = DateFormaterUtils.dateFormate(disponibilidade);

        assertEquals("20/10/2025 - 09:30 às 10:30", resultado);
    }

    @Test
    void testMontarDataHora() {
        LocalDateTime resultado = DateFormaterUtils.montarDataHora("2025-10-20", "14:45");

        assertEquals(LocalDateTime.of(2025, 10, 20, 14, 45), resultado);
    }

    @Test
    void testMontarDataHoraWithDifferentFormat() {
        LocalDateTime resultado = DateFormaterUtils.montarDataHora("2025-12-25", "23:59");

        assertEquals(LocalDateTime.of(2025, 12, 25, 23, 59), resultado);
    }

    @Test
    void testExtrairDatas() {
        DisponibilidadeDTO dto = new DisponibilidadeDTO(
            "2025-10-20",
            "09:00",
            "10:00"
        );

        DateFormaterUtils.DatasDisponibilidade resultado = DateFormaterUtils.extrairDatas(dto);

        assertEquals(LocalDateTime.of(2025, 10, 20, 9, 0), resultado.inicio);
        assertEquals(LocalDateTime.of(2025, 10, 20, 10, 0), resultado.fim);
    }

    @Test
    void testDatasDisponibilidadeConstructor() {
        LocalDateTime inicio = LocalDateTime.of(2025, 10, 21, 8, 0);
        LocalDateTime fim = LocalDateTime.of(2025, 10, 21, 9, 0);

        DateFormaterUtils.DatasDisponibilidade datas = new DateFormaterUtils.DatasDisponibilidade(inicio, fim);

        assertEquals(inicio, datas.inicio);
        assertEquals(fim, datas.fim);
    }

    @Test
    void testDateFormateWithMidnight() {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setHoraInicio(LocalDateTime.of(2025, 1, 1, 0, 0));
        disponibilidade.setHoraFim(LocalDateTime.of(2025, 1, 1, 1, 0));

        String resultado = DateFormaterUtils.dateFormate(disponibilidade);

        assertEquals("01/01/2025 - 00:00 às 01:00", resultado);
    }

    @Test
    void testDateFormateWithNoon() {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setHoraInicio(LocalDateTime.of(2025, 6, 15, 12, 0));
        disponibilidade.setHoraFim(LocalDateTime.of(2025, 6, 15, 13, 0));

        String resultado = DateFormaterUtils.dateFormate(disponibilidade);

        assertEquals("15/06/2025 - 12:00 às 13:00", resultado);
    }
}
