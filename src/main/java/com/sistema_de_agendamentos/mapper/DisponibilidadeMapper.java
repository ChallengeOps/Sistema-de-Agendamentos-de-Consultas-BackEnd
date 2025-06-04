package com.sistema_de_agendamentos.mapper;

import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeAgendarDTO;
import com.sistema_de_agendamentos.controller.dto.disponibilidade.DisponibilidadeListagemDTO;
import com.sistema_de_agendamentos.entity.Disponibilidade;
import org.springframework.stereotype.Service;

import static com.sistema_de_agendamentos.utils.DateFormaterUtils.dateFormate;

@Service
public class DisponibilidadeMapper {

    public DisponibilidadeListagemDTO toListagemDTO(Disponibilidade d) {
        return new DisponibilidadeListagemDTO(
                d.getId(),
                d.getHoraInicio().toLocalDate().toString(),
                d.getHoraInicio().toLocalTime().toString(),
                d.getHoraFim().toLocalTime().toString()
        );
    }

    public DisponibilidadeAgendarDTO toAgendarDTO(Disponibilidade d) {
        return new DisponibilidadeAgendarDTO(d.getId(), dateFormate(d));
    }
}