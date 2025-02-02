package br.com.asn.checkin_api.models.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.asn.checkin_api.models.Evento;
import br.com.asn.checkin_api.models.Participacao;
import br.com.asn.checkin_api.models.Usuario;
import br.com.asn.checkin_api.models.dto.ParticipacaoDTO;
import br.com.asn.checkin_api.repositories.EventoRepository;
import br.com.asn.checkin_api.repositories.UsuarioRepository;

@Mapper(componentModel = "spring")
public abstract class ParticipacaoMapper {

    @Autowired
    protected EventoRepository eventoRepository;
    @Autowired
    protected UsuarioRepository usuarioRepository;

    @Mapping(target = "eventoId", source = "evento.id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    public abstract ParticipacaoDTO toParticipacaoDTO(Participacao participacao);

    protected Evento getEvento(UUID eventoId) {
        return eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com o ID: " + eventoId));
    }

    protected Usuario getUsuario(UUID usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));
    }

}