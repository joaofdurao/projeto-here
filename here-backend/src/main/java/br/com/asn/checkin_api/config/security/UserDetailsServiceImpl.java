package br.com.asn.checkin_api.config.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.asn.checkin_api.models.Usuario;
import br.com.asn.checkin_api.repositories.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(username);

        if (!optUsuario.isPresent()) {
            throw new UsernameNotFoundException("Usuario nao encontrado.");
        }
        Usuario usuario = optUsuario.get();

        return new UserAuth(usuario);
    }

}
