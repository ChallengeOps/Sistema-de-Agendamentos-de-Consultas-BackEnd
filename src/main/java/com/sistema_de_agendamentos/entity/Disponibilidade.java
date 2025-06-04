package com.sistema_de_agendamentos.entity;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Disponibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime horaInicio;

    @Column(nullable = false)
    private LocalDateTime horaFim;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Usuario profissional;

    @OneToOne(mappedBy = "disponibilidade")
    private Agendamento agendamento;


}
