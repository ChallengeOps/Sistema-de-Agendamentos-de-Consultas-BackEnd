// src/main/java/com/sistema_de_agendamentos/utils/DateFormaterUtils.java
package com.sistema_de_agendamentos.utils;

import com.sistema_de_agendamentos.entity.Disponibilidade;
import org.springframework.stereotype.Service;

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
}