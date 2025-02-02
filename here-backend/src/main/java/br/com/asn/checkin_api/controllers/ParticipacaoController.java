package br.com.asn.checkin_api.controllers;

import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import br.com.asn.checkin_api.models.dto.ParticipacaoDTO;
import br.com.asn.checkin_api.services.ParticipacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/participacoes")
public class ParticipacaoController {

    private ParticipacaoService participacaoService;

    public ParticipacaoController(ParticipacaoService participacaoService) {
        this.participacaoService = participacaoService;
    }

    @PostMapping
    public ResponseEntity<ParticipacaoDTO> createParticipacao(
            @AuthenticationPrincipal UserAuth usuarioAuth,
            @Valid @RequestBody String eventoId) {
        UUID usuarioId = usuarioAuth.getUsuario().getId();
        ParticipacaoDTO novaParticipacaoDTO = participacaoService.createParticipacao(usuarioId,
                UUID.fromString(eventoId));
        return new ResponseEntity<>(novaParticipacaoDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ParticipacaoDTO> getParticipacaoByUsuarioEvento(
            @AuthenticationPrincipal UserAuth usuarioAuth,
            @RequestParam String strEventoId) {
        UUID usuarioId = usuarioAuth.getUsuario().getId();
        UUID eventoId = UUID.fromString(strEventoId);
        ParticipacaoDTO participacao = participacaoService.getParticipacaoByUsuarioEvento(usuarioId, eventoId);
        return ResponseEntity.ok(participacao);
    }

    @GetMapping("/qrcode/usuario/{strEventoId}")
    public ResponseEntity<byte[]> getParticipacaoQRCode(
            @AuthenticationPrincipal UserAuth usuarioAuth,
            @PathVariable String strEventoId) {
        UUID usuarioId = usuarioAuth.getUsuario().getId();
        UUID eventoId = UUID.fromString(strEventoId);
        byte[] qrCode = participacaoService.generateQRCodeForParticipacao(usuarioId, eventoId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }

    @PutMapping("/confirmar-presenca/{strParticipacaoId}")
    public ResponseEntity<String> confirmPresenca(@PathVariable String strParticipacaoId) {
        participacaoService.confirmPresenca(UUID.fromString(strParticipacaoId));
        return ResponseEntity.ok("Presença confirmada.");
    }

    @PutMapping("/enable/{strParticipacaoId}")
    public ResponseEntity<String> enableParticipacao(@PathVariable String strParticipacaoId) {
        participacaoService.enableParticipacao(UUID.fromString(strParticipacaoId));
        return ResponseEntity.ok("Participação habilitada com sucesso!");
    }

    @PutMapping("/disable/{strParticipacaoId}")
    public ResponseEntity<String> disableParticipacao(@PathVariable String strParticipacaoId) {
        participacaoService.disableParticipacao(UUID.fromString(strParticipacaoId));
        return ResponseEntity.ok("Participação desabilitada com sucesso!");
    }
}
