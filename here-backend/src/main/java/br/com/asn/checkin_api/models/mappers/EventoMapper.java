package br.com.asn.checkin_api.models.mappers;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.asn.checkin_api.models.Evento;
import br.com.asn.checkin_api.models.dto.EventoDTO;
import br.com.asn.checkin_api.models.dto.EventoLongDTO;
import br.com.asn.checkin_api.repositories.ParticipacaoRepository;

@Mapper(componentModel = "spring")
public abstract class EventoMapper {

    @Autowired
    protected ParticipacaoRepository participacaoRepository;

    public abstract Evento toEvento(EventoDTO eventoDTO);

    public abstract EventoDTO toEventoDTO(Evento evento);

    public abstract List<EventoDTO> toEventoDTOList(List<Evento> eventos);

    @Mapping(target = "participacaoId", expression = "java(getParticipacaoId(evento, usuarioId))")
    public abstract EventoLongDTO toEventoLongDTO(Evento evento, UUID usuarioId);

    protected UUID getParticipacaoId(Evento evento, UUID usuarioId) {
        return participacaoRepository.findIdByUsuarioIdAndEventoId(evento.getId(), usuarioId)
                .orElse(null);
    }
}
