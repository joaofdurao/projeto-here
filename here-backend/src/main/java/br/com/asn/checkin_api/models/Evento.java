package br.com.asn.checkin_api.models;


import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eventos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Column
    private LocalDateTime dataHora;

    @Column
    private Integer horaAula;

    @Column
    private String curso;

    @Column
    private String local;

    @Column
    private String palestrante;
    
    @Column
    private String profissaoPalestrante;
    
    @Column
    private Boolean status;

}
