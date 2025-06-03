package com.sistema_de_agendamentos.utils;

import com.sistema_de_agendamentos.entity.Disponibilidade;
import org.springframework.stereotype.Service;

@Service
public class DateFormaterUtils {

    public static String dateFormate(Disponibilidade disponibilidade){
        //faca um metodo pra retornar a data nesse padrao 02/06/2025 (Segunda-feira) - 08:00 às 12:00\
        var diaDaSemana = disponibilidade.getDiaDeSemana().getValor();
        var horaInicio = disponibilidade.getHoraInicio().toLocalTime().toString();
        var horaFim = disponibilidade.getHoraFim().toLocalTime().toString();
        return String.format("%s - %s às %s", diaDaSemana, horaInicio, horaFim);
    }
}
