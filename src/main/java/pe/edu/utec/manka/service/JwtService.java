package pe.edu.utec.manka.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import pe.edu.utec.manka.entity.Usuario;
import java.util.UUID;

import java.time.Instant;
import java.util.List;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long accessTokenSeconds;

    public JwtService(JwtEncoder jwtEncoder, @Value("${jwt.access-token-seconds:36000}") long accessTokenSeconds) {
        this.jwtEncoder = jwtEncoder;
        this.accessTokenSeconds = accessTokenSeconds;
    }

    public String generateAccessToken(Usuario user) {
        Instant now = Instant.now();

        List<String> roles = user.getRoles().stream().map(role -> role.getName().name())
                .sorted().toList();

        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("manka").issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenSeconds)).subject(user.getEmail()).id(UUID.randomUUID()
                .toString()).claim("userId", user.getId()).claim("roles", roles).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
