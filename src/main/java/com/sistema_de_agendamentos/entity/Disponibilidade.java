package com.sistema_de_agendamentos.entity;

import ch.qos.logback.core.net.server.Client;

import java.time.LocalDateTime;

public class Disponibilidade {

    private Integer id;
    private Usuario profissional;
    private DiaDeSemana diaDeSemana;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;


    enum DiaDeSemana {
        DOMINGO,
        SEGUNDA,
        TERCA,
        QUARTA,
        QUINTA,
        SEXTA,
        SABADO
    }
}
