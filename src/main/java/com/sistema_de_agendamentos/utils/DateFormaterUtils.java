// src/main/java/com/sistema_de_agendamentos/utils/DateFormaterUtils.java
package com.sistema_de_agendamentos.utils;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateFormaterUtils {

    public static String dateFormate(Disponibilidade disponibilidade){
        var dataFormatada = disponibilidade.getHoraInicio()
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        var horaInicio = disponibilidade.getHoraInicio()
                .toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"));
        var horaFim = disponibilidade.getHoraFim()
                .toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"));
        return String.format("%s - %s às %s", dataFormatada, horaInicio, horaFim);
    }

    public static LocalDateTime montarDataHora(String data, String hora) {
        return LocalDateTime.of(LocalDate.parse(data), LocalTime.parse(hora));
    }

    public static DatasDisponibilidade extrairDatas(DisponibilidadeDTO dto) {
        var inicio = montarDataHora(dto.data(), dto.horarioInicio());
        var fim = montarDataHora(dto.data(), dto.horarioFim());
        return new DatasDisponibilidade(inicio, fim);
    }

    public static class DatasDisponibilidade{
        public final LocalDateTime inicio;
        public final LocalDateTime fim;

        public DatasDisponibilidade(LocalDateTime inicio, LocalDateTime fim) {
            this.inicio = inicio;
            this.fim = fim;
        }
    }
}