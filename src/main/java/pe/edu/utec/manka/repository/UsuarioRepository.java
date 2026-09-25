package pe.edu.utec.manka.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utec.manka.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"despensa"})
    Optional<Usuario> findWithDespensaByEmail(String email);

    @EntityGraph(attributePaths = {"favoritos"})
    Optional<Usuario> findWithFavoritosByEmail(String email);
}
