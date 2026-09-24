package pe.edu.utec.manka.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import pe.edu.utec.manka.dto.LoginRequestDto;
import pe.edu.utec.manka.dto.LoginResponseDto;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.exception.BusinessRuleException;
import pe.edu.utec.manka.repository.UsuarioRepository;

import java.time.Instant;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        String email = request.getEmail().trim().toLowerCase();
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessRuleException("Unknown email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessRuleException("Incorrect password");
        }

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("manka")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(36000))
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponseDto(token);
    }
}
