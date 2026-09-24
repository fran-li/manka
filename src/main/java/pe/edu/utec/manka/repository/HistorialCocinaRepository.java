package pe.edu.utec.manka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utec.manka.entity.HistorialCocina;

import java.time.LocalDateTime;
import java.util.List;

public interface HistorialCocinaRepository extends JpaRepository<HistorialCocina, Long> {

    @Query("""
            select h from HistorialCocina h
            join fetch h.plato p
            left join fetch p.categoriaProteina
            where h.usuario.id = :userId and h.cocinadoEn >= :fromDate
            order by h.cocinadoEn desc
            """)
    List<HistorialCocina> findRecentByUserId(@Param("userId") Long userId,
                                              @Param("fromDate") LocalDateTime fromDate);

    @Query("""
            select h from HistorialCocina h
            join fetch h.plato p
            where h.usuario.id = :userId
            order by h.cocinadoEn desc
            """)
    List<HistorialCocina> findAllByUserId(@Param("userId") Long userId);
}
