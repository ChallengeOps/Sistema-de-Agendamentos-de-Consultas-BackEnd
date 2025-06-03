package com.sistema_de_agendamentos.mapper;

import com.sistema_de_agendamentos.config.security.dto.RegisterRequestDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioDTO;
import com.sistema_de_agendamentos.controller.dto.usuario.UsuarioRegisterDTO;
import com.sistema_de_agendamentos.entity.Usuario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario fromRegisterRequestDTO(RegisterRequestDTO dto);

    UsuarioDTO fromEntity(Usuario usuario);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Usuario updateFromDTO(UsuarioDTO dto, @MappingTarget Usuario usuario);

    Usuario fromRegisterDTO(UsuarioRegisterDTO usuarioDTO);
}
