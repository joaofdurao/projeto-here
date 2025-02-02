package br.com.asn.checkin_api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.asn.checkin_api.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{

    Optional<Usuario> findByEmail(String email);

}
