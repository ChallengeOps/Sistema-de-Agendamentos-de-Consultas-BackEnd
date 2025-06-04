package com.sistema_de_agendamentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Agendamento> agendamentos;

    @OneToMany(mappedBy = "profissional", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Servico> servicos;

    @OneToMany(mappedBy = "profissional", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Disponibilidade> disponibilidades;



    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClienteTipo acesso;

    public enum ClienteTipo{
        CLIENTE,
        PROFISSIONAL;

        //faca aqui um getnome do enum ClienteTipo
        public String getNome() {
            return this.name();
        }

    }
}
