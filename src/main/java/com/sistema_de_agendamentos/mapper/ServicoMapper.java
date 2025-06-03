package com.sistema_de_agendamentos.mapper;


import com.sistema_de_agendamentos.controller.dto.servico.ServicoDTO;
import com.sistema_de_agendamentos.entity.Servico;
import com.sistema_de_agendamentos.entity.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ServicoMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(ServicoDTO dto, @MappingTarget Servico servico);

    @Mapping(target = "profissional", source = "profissional")
    Servico fromDTO(ServicoDTO dto, Usuario profissional);

}