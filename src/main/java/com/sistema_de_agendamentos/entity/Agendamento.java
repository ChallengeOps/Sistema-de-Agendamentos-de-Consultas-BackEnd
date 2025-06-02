package com.sistema_de_agendamentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Usuario profissional;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @OneToOne
    @JoinColumn(name = "disponibilidade_id", unique = true)
    private Disponibilidade disponibilidade;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDENTE,
        CONFIRMADO,
        CANCELADO,
        CONCLUIDO;

        public static Status fromString(String status) {
            if (status == null) {
                throw new IllegalArgumentException("Status não pode ser nulo");
            }
            try {
                return Status.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Status inválido: " + status);
            }
        }
    }
    // Getters and Setters can be generated or manually added here
}
