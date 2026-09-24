package pe.edu.utec.manka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utec.manka.entity.Ingrediente;

import java.util.List;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    List<Ingrediente> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);
}
