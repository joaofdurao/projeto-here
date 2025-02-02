package br.com.asn.checkin_api.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.asn.checkin_api.models.Evento;
import br.com.asn.checkin_api.models.dto.EventoDTO;
import br.com.asn.checkin_api.models.dto.EventoLongDTO;
import br.com.asn.checkin_api.models.dto.EventoUpdateDTO;
import br.com.asn.checkin_api.models.mappers.EventoMapper;
import br.com.asn.checkin_api.repositories.EventoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EventoService {

    private final EventoMapper eventoMapper;
    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository, EventoMapper eventoMapper) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
    }

    @Transactional
    public EventoDTO createEvento(EventoDTO eventoDTO) {
        Evento evento = eventoMapper.toEvento(eventoDTO);
        evento.setStatus(true);
        eventoRepository.save(evento);
        return eventoMapper.toEventoDTO(evento);
    }

    public List<EventoDTO> getAllEventos() {
        List<Evento> eventos = eventoRepository.findAll();
        return eventoMapper.toEventoDTOList(eventos);
    }

    public List<EventoDTO> getAllEventosByStatus(Boolean status) {
        List<Evento> eventos;
    
        if (status) {
            eventos = eventoRepository.findByStatusTrue();
        } else {
            eventos = eventoRepository.findByStatusFalse();
        }
    
        if (eventos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum evento encontrado com o status especificado.");
        }
    
        return eventos.stream()
                .map(eventoMapper::toEventoDTO)
                .collect(Collectors.toList());
    }

    public EventoLongDTO getEventoById(UUID usuarioId, UUID eventoId) {
        Evento evento = findEventoById(eventoId);
        return eventoMapper.toEventoLongDTO(evento, usuarioId);
    }

    @Transactional
    public EventoDTO updateEvento(UUID id, EventoUpdateDTO eventoUpdateDTO) {
        Evento evento = findEventoById(id);

        if (eventoUpdateDTO.getTitulo() != null) {
            evento.setTitulo(eventoUpdateDTO.getTitulo());
        }
        if (eventoUpdateDTO.getDescricao() != null) {
            evento.setDescricao(eventoUpdateDTO.getDescricao());
        }
        if (eventoUpdateDTO.getDataHora() != null) {
            evento.setDataHora(eventoUpdateDTO.getDataHora());
        }
        if (eventoUpdateDTO.getHoraAula() != null) {
            evento.setHoraAula(eventoUpdateDTO.getHoraAula());
        }
        if (eventoUpdateDTO.getCurso() != null) {
            evento.setCurso(eventoUpdateDTO.getCurso());
        }
        if (eventoUpdateDTO.getLocal() != null) {
            evento.setLocal(eventoUpdateDTO.getLocal());
        }
        if (eventoUpdateDTO.getPalestrante() != null) {
            evento.setPalestrante(eventoUpdateDTO.getPalestrante());
        }
        if (eventoUpdateDTO.getProfissaoPalestrante() != null) {
            evento.setProfissaoPalestrante(eventoUpdateDTO.getProfissaoPalestrante());
        }

        eventoRepository.save(evento);
        return eventoMapper.toEventoDTO(evento);
    }

    @Transactional
    public void enableEvento(UUID id) {
        Evento evento = findEventoById(id);
        evento.setStatus(true);
        eventoRepository.save(evento);
    }

    @Transactional
    public void disableEvento(UUID id) {
        Evento evento = findEventoById(id);
        evento.setStatus(false);
        eventoRepository.save(evento);
    }

    private Evento findEventoById(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado com o id: " + id));
    }
}
