package com.sistema_de_agendamentos.mapper;

import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.entity.Servico;
import com.sistema_de_agendamentos.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public void updateFromDTO(ServicoDTO dto, Servico servico) {
        if (dto == null || servico == null) return;

        // Só atualiza se os campos do DTO não forem nulos
        if (dto.nome() != null) {
            servico.setNome(dto.nome());
        }

        if (dto.descricao() != null) {
            servico.setDescricao(dto.descricao());
        }

        if (dto.duracaoEmMinutos() != null) {
            servico.setDuracaoEmMinutos(dto.duracaoEmMinutos());
        }
    }

    public Servico fromDTO(ServicoDTO dto, Usuario profissional) {
        if (dto == null || profissional == null) return null;

        Servico servico = new Servico();
        servico.setNome(dto.nome());
        servico.setDescricao(dto.descricao());
        servico.setDuracaoEmMinutos(dto.duracaoEmMinutos());
        servico.setProfissional(profissional);

        // Lista de agendamentos começa nula
        servico.setAgendamentos(null);

        return servico;
    }
}
