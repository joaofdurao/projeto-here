package br.com.asn.checkin_api.models.mappers;

import org.mapstruct.Mapper;

import br.com.asn.checkin_api.models.Usuario;
import br.com.asn.checkin_api.models.dto.UsuarioDTO;
import br.com.asn.checkin_api.models.dto.UsuarioReadDTO;

@Mapper(componentModel = "spring")
public abstract class UsuarioMapper {

    public abstract Usuario toUsuario(UsuarioDTO usuarioDTO);

    public abstract UsuarioReadDTO toUsuarioReadDTO(Usuario usuario);

}