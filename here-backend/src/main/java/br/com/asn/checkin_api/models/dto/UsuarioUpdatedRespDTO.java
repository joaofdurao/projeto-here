package br.com.asn.checkin_api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioUpdatedRespDTO {

    private UsuarioReadDTO usuarioReadDTO;

    private String token;

}
