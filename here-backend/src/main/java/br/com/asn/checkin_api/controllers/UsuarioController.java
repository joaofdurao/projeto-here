package br.com.asn.checkin_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.asn.checkin_api.config.security.UserAuth;
import br.com.asn.checkin_api.models.dto.EventoDTO;
import br.com.asn.checkin_api.models.dto.UsuarioAuthDTO;
import br.com.asn.checkin_api.models.dto.UsuarioDTO;
import br.com.asn.checkin_api.models.dto.UsuarioReadDTO;
import br.com.asn.checkin_api.models.dto.UsuarioUpdateDTO;
import br.com.asn.checkin_api.models.dto.UsuarioUpdatedRespDTO;
import br.com.asn.checkin_api.services.ParticipacaoService;
import br.com.asn.checkin_api.services.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;
    private ParticipacaoService participacaoService;

    public UsuarioController(UsuarioService usuarioService, ParticipacaoService participacaoService) {
        this.usuarioService = usuarioService;
        this.participacaoService = participacaoService;
    }

    @PostMapping
    public ResponseEntity<UsuarioReadDTO> registerUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioReadDTO response = usuarioService.createUsuario(usuarioDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> getUsuarioByEmailPassword(@Valid @RequestBody UsuarioAuthDTO usuarioAuthDTO) {
        String token = usuarioService.verifyUsuario(usuarioAuthDTO.getEmail(), usuarioAuthDTO.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/detail")
    public ResponseEntity<UsuarioReadDTO> getUsuarioById(@AuthenticationPrincipal UserAuth usuarioAuth) {
        UUID usuarioId = usuarioAuth.getUsuario().getId();
        UsuarioReadDTO usuarioRDTO = usuarioService.getUsuarioById(usuarioId);
        return ResponseEntity.ok(usuarioRDTO);
    }

    @GetMapping("/detail/eventos")
    public ResponseEntity<List<EventoDTO>> listEventosFromUser(@AuthenticationPrincipal UserAuth usuarioAuth) {
        UUID usuarioId = usuarioAuth.getUsuario().getId();
        List<EventoDTO> eventos = participacaoService.listEventosPorUsuarioId(usuarioId);
        return ResponseEntity.ok(eventos);
    }

    @PutMapping("/detail")
    public ResponseEntity<UsuarioUpdatedRespDTO> updateUsuario(@AuthenticationPrincipal UserAuth usuarioAuth,
            @RequestBody @Valid UsuarioUpdateDTO usuarioUpdateDTO) {
        UUID usuarioId = usuarioAuth.getUsuario().getId();
        UsuarioUpdatedRespDTO usuarioURDTO = usuarioService.updateUsuario(usuarioId, usuarioUpdateDTO);
        return ResponseEntity.ok(usuarioURDTO);
    }

    @PutMapping("/enable")
    public ResponseEntity<String> enableUsuario(@AuthenticationPrincipal UserAuth usuarioAuth) {
        UUID usuarioId = usuarioAuth.getUsuario().getId();
        usuarioService.enableUsuario(usuarioId);
        return ResponseEntity.ok("Usuário habilitado com sucesso!");
    }

    @PutMapping("/disable")
    public ResponseEntity<String> disableUsuario(@AuthenticationPrincipal UserAuth usuarioAuth) {
        UUID usuarioId = usuarioAuth.getUsuario().getId();
        usuarioService.disableUsuario(usuarioId);
        return ResponseEntity.ok("Usuário desabilitado com sucesso!");
    }

}