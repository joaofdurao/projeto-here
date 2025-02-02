package br.com.asn.checkin_api.models.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EventoDTO {

    private UUID id;

    @NotBlank(message = "O título do evento é obrigatório.")
    @Size(min = 5, max = 100, message = "O título deve ter entre 5 e 100 caracteres.")
    private String titulo;

    @NotBlank(message = "A descrição do evento é obrigatória.")
    @Size(min = 10, max = 500, message = "A descrição deve ter entre 10 e 500 caracteres.")
    private String descricao;

    @NotNull(message = "A data e hora do evento são obrigatórios.")
    @Future(message = "A data/hora deve ser no futuro.")
    private LocalDateTime dataHora;

    @NotNull(message = "A duração do evento é obrigatória.")
    @Min(value = 1, message = "A duração do evento deve ser no mínimo 1 hora.")
    private Integer horaAula;

    @Size(max = 100, message = "O curso deve ter no máximo 100 caracteres.")
    private String curso;

    @Size(max = 100, message = "O local deve ter no máximo 100 caracteres.")
    private String local;

    @Size(max = 100, message = "O palestrante deve ter no máximo 100 caracteres.")
    private String palestrante;

    @Size(max = 100, message = "A profissão do palestrante deve ter no máximo 100 caracteres.")
    private String profissaoPalestrante;

    private Boolean status;

}
