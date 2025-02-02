package br.com.asn.checkin_api.services;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import br.com.asn.checkin_api.config.exceptions.QRCodeGenerationException;
import br.com.asn.checkin_api.models.Evento;
import br.com.asn.checkin_api.models.Participacao;
import br.com.asn.checkin_api.models.Usuario;
import br.com.asn.checkin_api.models.dto.EventoDTO;
import br.com.asn.checkin_api.models.dto.ParticipacaoDTO;
import br.com.asn.checkin_api.models.mappers.EventoMapper;
import br.com.asn.checkin_api.models.mappers.ParticipacaoMapper;
import br.com.asn.checkin_api.repositories.EventoRepository;
import br.com.asn.checkin_api.repositories.ParticipacaoRepository;
import br.com.asn.checkin_api.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ParticipacaoService {

    private final ParticipacaoMapper participacaoMapper;
    private final ParticipacaoRepository participacaoRepository;
    private final UsuarioRepository userRepository;
    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;

    
    public ParticipacaoService(ParticipacaoMapper participacaoMapper, ParticipacaoRepository participacaoRepository,
            UsuarioRepository userRepository, EventoRepository eventoRepository, EventoMapper eventoMapper) {
        this.participacaoMapper = participacaoMapper;
        this.participacaoRepository = participacaoRepository;
        this.userRepository = userRepository;
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
    }

    public List<EventoDTO> listEventosPorUsuarioId(UUID usuarioId) {
        List<Evento> eventos = participacaoRepository.findEventosByUsuarioId(usuarioId);
        if (eventos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum evento encontrado.");
        }
        return eventos.stream()
                .map(eventoMapper::toEventoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipacaoDTO createParticipacao(UUID usuarioId, UUID eventoId) {
        if (usuarioId == null || eventoId == null) {
            throw new IllegalArgumentException("usuarioId e eventoId não podem ser nulos");
        }

        Optional<Participacao> participacaoExistente = participacaoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId);

        if (participacaoExistente.isPresent()) {
            Participacao participacao = participacaoExistente.get();
            if (participacao.isStatus()) {
                throw new IllegalArgumentException("Participacao já cadastrada.");
            } else {
                enableParticipacao(participacao.getId());
                return participacaoMapper.toParticipacaoDTO(participacao);
            }
        } else {
            Usuario usuario = userRepository.findById(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            Evento evento = eventoRepository.findById(eventoId)
                    .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

            Participacao novaParticipacao = new Participacao();
            novaParticipacao.setUsuario(usuario);
            novaParticipacao.setEvento(evento);
            novaParticipacao.setStatus(true);
            novaParticipacao.setPresenca(false);
            participacaoRepository.save(novaParticipacao);

            return participacaoMapper.toParticipacaoDTO(novaParticipacao);
        }
    }


    public byte[] generateQRCodeForParticipacao(UUID usuarioId, UUID eventoId) {

        Participacao participacao = participacaoRepository
                .findByUsuarioIdAndEventoId(usuarioId, eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada"));

        UUID participacaoId = participacao.getId();

        return generateQRCodeImage(participacaoId.toString(), 200, 200);
    }

    private byte[] generateQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hintMap);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        } catch (Exception e) {
            throw new QRCodeGenerationException("Erro ao gerar o QRCode", e);
        }
    }

    @Transactional
    public ParticipacaoDTO getParticipacaoByUsuarioEvento(UUID usuarioId, UUID eventoId) {
        Participacao participacao = participacaoRepository.findByUsuarioIdAndEventoId(usuarioId, eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada"));

        return participacaoMapper.toParticipacaoDTO(participacao);
    }

    @Transactional
    public void confirmPresenca(UUID id) {
        Participacao participacao = participacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada"));

        participacao.setPresenca(true);
        participacaoRepository.save(participacao);
    }

    @Transactional
    public void enableParticipacao(UUID id) {
        Participacao participacao = participacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada"));

        participacao.setStatus(true);
        participacaoRepository.save(participacao);
    }

    @Transactional
    public void disableParticipacao(UUID id) {
        Participacao participacao = participacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada"));

        participacao.setStatus(false);
        participacaoRepository.save(participacao);
    }

}
