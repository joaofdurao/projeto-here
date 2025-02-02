package br.com.asn.checkin_api.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioUpdateDTO {

    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String name;

    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    private String password;

    @Size(max = 100, message = "O curso deve ter no máximo 100 caracteres.")
    private String curso;

    @Email(message = "O e-mail deve ser válido.")
    private String email;

    private String matricula;

}
