package br.com.asn.checkin_api.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.asn.checkin_api.config.exceptions.UnauthorizedException;
import br.com.asn.checkin_api.config.security.JWTService;
import br.com.asn.checkin_api.models.Usuario;
import br.com.asn.checkin_api.models.dto.UsuarioDTO;
import br.com.asn.checkin_api.models.dto.UsuarioReadDTO;
import br.com.asn.checkin_api.models.dto.UsuarioUpdateDTO;
import br.com.asn.checkin_api.models.dto.UsuarioUpdatedRespDTO;
import br.com.asn.checkin_api.models.mappers.UsuarioMapper;
import br.com.asn.checkin_api.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private UsuarioMapper usuarioMapper;
    private JWTService jwtService;
    AuthenticationManager authManager;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioMapper usuarioMapper, JWTService jwtService, AuthenticationManager authManager,
            UsuarioRepository usuarioRepository) {
        this.usuarioMapper = usuarioMapper;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.usuarioRepository = usuarioRepository;
    }

    private boolean isEmailInstitucional(String email) {
        return email.endsWith("@faculdadedamas.edu.br");
    }

    @Transactional
    public UsuarioReadDTO createUsuario(UsuarioDTO usuarioDTO) {
        if (!isEmailInstitucional(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("E-mail não é institucional.");
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioDTO.getEmail());

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();

            if (Boolean.FALSE.equals(usuario.getStatus())) {
                enableUsuario(usuario.getId());
                return usuarioMapper.toUsuarioReadDTO(usuario);
            } else {
                throw new IllegalArgumentException("Usuário já cadastrado.");
            }
        } else {

            Usuario novoUsuario = usuarioMapper.toUsuario(usuarioDTO);
            novoUsuario.setPassword(encoder.encode(novoUsuario.getPassword()));
            novoUsuario.setStatus(true);
            usuarioRepository.save(novoUsuario);
            return usuarioMapper.toUsuarioReadDTO(novoUsuario);
        }
    }

    public UsuarioReadDTO getUsuarioById(UUID id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toUsuarioReadDTO)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

    public String verifyUsuario(String email, String password) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return jwtService.generateToken(email);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Credenciais inválidas.");
        }
    }

    @Transactional
    public UsuarioUpdatedRespDTO updateUsuario(UUID id, UsuarioUpdateDTO usuarioUpdateDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (usuarioUpdateDTO.getName() != null) {
            usuario.setName(usuarioUpdateDTO.getName());
        }
        if (usuarioUpdateDTO.getEmail() != null) {
            if (!isEmailInstitucional(usuarioUpdateDTO.getEmail())) {
                throw new IllegalArgumentException("E-mail não é institucional.");
            }

            if (usuarioUpdateDTO.getEmail().equals(usuario.getEmail())) {
                throw new IllegalArgumentException("E-mail já utilizado.");
            } else {
                verificarEmailUnico(usuarioUpdateDTO.getEmail());
                usuario.setEmail(usuarioUpdateDTO.getEmail());
            }
        }
        if (usuarioUpdateDTO.getMatricula() != null) {
            usuario.setMatricula(usuarioUpdateDTO.getMatricula());
        }
        if (usuarioUpdateDTO.getCurso() != null) {
            usuario.setCurso(usuarioUpdateDTO.getCurso());
        }
        if (usuarioUpdateDTO.getPassword() != null) {
            usuario.setPassword(encoder.encode(usuarioUpdateDTO.getPassword()));
        }

        usuarioRepository.save(usuario);
        UsuarioReadDTO usuarioReadDTO = usuarioMapper.toUsuarioReadDTO(usuario);
        String token = jwtService.generateToken(usuario.getEmail());
        return new UsuarioUpdatedRespDTO(usuarioReadDTO, token);
    }

    @Transactional
    public void enableUsuario(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        usuario.setStatus(true);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void disableUsuario(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        usuario.setStatus(false);
        usuarioRepository.save(usuario);
    }

    private void verificarEmailUnico(String email) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
    }
}