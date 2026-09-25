package pe.edu.utec.manka.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.entity.RefreshToken;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.exception.InvalidTokenException;
import pe.edu.utec.manka.exception.RefreshTokenExpiredException;
import pe.edu.utec.manka.repository.RefreshTokenRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenSeconds;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${jwt.refresh-token-seconds:604800}") long refreshTokenSeconds) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenSeconds = refreshTokenSeconds;
    }

    @Transactional
    public String create(Usuario user) {
        String rawToken = UUID.randomUUID() + "." + UUID.randomUUID();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(hash(rawToken));
        refreshToken.setUsuario(user);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plusSeconds(refreshTokenSeconds));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    @Transactional
    public Usuario consume(String rawToken) {
        RefreshToken refreshToken = find(rawToken);

        if (refreshToken.isRevoked()) {
            throw new InvalidTokenException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new RefreshTokenExpiredException("Refresh token has expired");
        }

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getUsuario();
    }

    @Transactional
    public void revoke(String rawToken) {
        RefreshToken refreshToken = find(rawToken);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken find(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new InvalidTokenException("Refresh token is required");
        }

        return refreshTokenRepository.findByTokenHash(hash(rawToken))
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encoded);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
