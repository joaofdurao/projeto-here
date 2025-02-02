package br.com.asn.checkin_api.models.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UsuarioReadDTO {

    private UUID id;

    private String name;

    private String curso;

    private String email;

    private String matricula;

    private boolean status;

}
