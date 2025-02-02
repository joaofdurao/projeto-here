package br.com.asn.checkin_api.models.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParticipacaoDTO {

    private UUID id;

    @NotNull(message = "A presença deve ser informada.")
    private boolean presenca;

    @NotNull(message = "O status da participação deve ser informado.")
    private boolean status;

    @NotNull(message = "O evento associado à participação é obrigatório.")
    private UUID eventoId;

    @NotNull(message = "O usuário associado à participação é obrigatório.")
    private UUID usuarioId;

}
