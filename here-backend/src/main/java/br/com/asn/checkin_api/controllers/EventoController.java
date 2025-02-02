package br.com.asn.checkin_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.asn.checkin_api.config.security.UserAuth;
import br.com.asn.checkin_api.models.dto.EventoDTO;
import br.com.asn.checkin_api.models.dto.EventoLongDTO;
import br.com.asn.checkin_api.models.dto.EventoUpdateDTO;
import br.com.asn.checkin_api.services.EventoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<EventoDTO> createEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        EventoDTO response = eventoService.createEvento(eventoDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{strEventoId}")
    public ResponseEntity<EventoLongDTO> getEventoById(@AuthenticationPrincipal UserAuth usuarioAuth,
                                                       @PathVariable String strEventoId) {
        UUID usuarioId = usuarioAuth.getUsuario().getId();
        EventoLongDTO eventoLongDTO = eventoService.getEventoById(usuarioId, UUID.fromString(strEventoId));
        return ResponseEntity.ok(eventoLongDTO);
    }

    @GetMapping
    public ResponseEntity<List<EventoDTO>> getAllEventos(@RequestParam(required = false) Boolean status) {
        List<EventoDTO> eventosDTO;

        if (status != null) {
            eventosDTO = eventoService.getAllEventosByStatus(status);
        } else {
            eventosDTO = eventoService.getAllEventos();
        }

        return ResponseEntity.ok(eventosDTO);
    }

    @PutMapping("/{strEventoId}")
    public ResponseEntity<EventoDTO> updateEvento(@PathVariable String strEventoId,
            @RequestBody @Valid EventoUpdateDTO eventoUpdateDTO) {
        EventoDTO response = eventoService.updateEvento(UUID.fromString(strEventoId), eventoUpdateDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/enable/{strEventoId}")
    public ResponseEntity<String> enableEvento(@PathVariable String strEventoId) {
        eventoService.enableEvento(UUID.fromString(strEventoId));
        return ResponseEntity.ok("Evento habilitado com sucesso!");
    }

    @PutMapping("/disable/{strEventoId}")
    public ResponseEntity<String> disableEvento(@PathVariable String strEventoId) {
        eventoService.disableEvento(UUID.fromString(strEventoId));
        return ResponseEntity.ok("Evendo desabilitado com sucesso!");
    }

}
