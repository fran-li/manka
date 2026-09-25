package pe.edu.utec.manka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utec.manka.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
