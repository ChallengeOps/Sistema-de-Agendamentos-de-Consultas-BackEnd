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
    @Enumerated(EnumType.STRING)
    private DiaDeSemana diaDeSemana;

    @Column(nullable = false)
    private LocalDateTime horaInicio;

    @Column(nullable = false)
    private LocalDateTime horaFim;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Usuario profissional;

    @OneToOne(mappedBy = "disponibilidade")
    private Agendamento agendamento;


    public enum DiaDeSemana {
        DOMINGO,
        SEGUNDA,
        TERCA,
        QUARTA,
        QUINTA,
        SEXTA,
        SABADO;

        public static DiaDeSemana fromString(String dia) {
            if (dia == null) {
                throw new IllegalArgumentException("Dia da semana não pode ser nulo");
            }
            try {
                return DiaDeSemana.valueOf(dia.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Dia da semana inválido: " + dia);
            }
        }

        //
        public String getValor() {
            return this.name().toLowerCase();
        }
    }
}
