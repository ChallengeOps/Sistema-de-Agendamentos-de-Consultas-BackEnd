package com.sistema_de_agendamentos.mapper;

import com.sistema_de_agendamentos.controller.dto.UsuarioDTO;
import com.sistema_de_agendamentos.controller.dto.UsuarioRegisterDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO fromEntity(Usuario usuario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Usuario updateFromDTO(UsuarioDTO dto, @MappingTarget Usuario usuario);

    Usuario fromDTO(UsuarioDTO usuarioDTO);

    Usuario fromRegisterDTO(UsuarioRegisterDTO usuarioDTO);
}
