package com.sistema_de_agendamentos.config.security;

import com.sistema_de_agendamentos.entity.Usuario;
import com.sistema_de_agendamentos.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public ResponseDTO login(LoginRequestDTO body){
        Usuario user = this.repository
                .findByEmail(body.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        if (passwordEncoder.matches(body.password(), user.getPassword())){
            var token = tokenService.generateToken(user);
            return new ResponseDTO(user.getName(), token);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseDTO register(RegisterRequestDTO body){
        var user = repository.findByEmail(body.email());
        if (user.isEmpty()){
            var newUser = new Usuario();
            newUser.setEmail(body.email());
            newUser.setNome(body.name());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            var token = tokenService.generateToken(newUser);
            repository.save(newUser);
            return new ResponseDTO(newUser.getNome(), token);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}