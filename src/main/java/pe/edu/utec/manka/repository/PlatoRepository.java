package pe.edu.utec.manka.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utec.manka.entity.Plato;

import java.util.List;
import java.util.Optional;

public interface PlatoRepository extends JpaRepository<Plato, Long> {

    @Query("""
            select distinct p from Plato p
            left join fetch p.categoriaProteina
            left join fetch p.ingredientes pi
            left join fetch pi.ingrediente i
            left join fetch i.parent
            where p.tiempoPreparacion <= :maxMinutes
            """)
    List<Plato> findCandidates(@Param("maxMinutes") Integer maxMinutes);

    @Override
    @EntityGraph(attributePaths = {"categoriaProteina", "ingredientes", "ingredientes.ingrediente", "ingredientes.ingrediente.parent"})
    Optional<Plato> findById(Long id);

    @EntityGraph(attributePaths = {"categoriaProteina"})
    List<Plato> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);
}
