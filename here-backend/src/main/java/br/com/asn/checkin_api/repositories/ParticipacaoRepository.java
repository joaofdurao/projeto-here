
package br.com.asn.checkin_api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.asn.checkin_api.models.Evento;
import br.com.asn.checkin_api.models.Participacao;

public interface ParticipacaoRepository extends JpaRepository<Participacao, UUID> {

    Optional<Participacao> findByUsuarioIdAndEventoId(UUID usuarioId, UUID eventoId);

    @Query("SELECT p.evento FROM Participacao p WHERE p.usuario.id = :usuarioId AND p.status = true")
    List<Evento> findEventosByUsuarioId(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT p.id FROM Participacao p where p.usuario.id = :usuarioId AND p.evento.id = :eventoId AND p.status = true")
    Optional<UUID> findIdByUsuarioIdAndEventoId(@Param("eventoId") UUID eventoId, @Param("usuarioId") UUID usuarioId);

}