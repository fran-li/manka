package pe.edu.utec.manka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utec.manka.entity.Rol;
import pe.edu.utec.manka.entity.TipoRol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByName(TipoRol name);
}
