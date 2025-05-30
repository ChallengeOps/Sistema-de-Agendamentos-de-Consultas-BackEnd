package com.sistema_de_agendamentos.entity;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class Disponibilidade {

    private Integer id;
    private DiaDeSemana diaDeSemana;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;



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
