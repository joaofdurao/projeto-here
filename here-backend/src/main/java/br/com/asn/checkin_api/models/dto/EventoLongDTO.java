package br.com.asn.checkin_api.models.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class EventoLongDTO {

    private UUID id;

    private String titulo;

    private String descricao;

    private LocalDateTime dataHora;

    private Integer horaAula;

    private String curso;

    private String local;

    private String palestrante;

    private String profissaoPalestrante;

    private Boolean status;

    private UUID participacaoId;

}
