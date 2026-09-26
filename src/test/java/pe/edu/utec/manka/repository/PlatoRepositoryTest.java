package pe.edu.utec.manka.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import pe.edu.utec.manka.AbstractContainerBaseTest;
import pe.edu.utec.manka.entity.Plato;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlatoRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    private PlatoRepository platoRepository;

    @Test
    void shouldFindOnlyCandidatesWithinAvailableMinutes() {
        List<Plato> candidates = platoRepository.findCandidates(30);

        assertFalse(candidates.isEmpty());
        assertTrue(candidates.stream().allMatch(p -> p.getTiempoPreparacion() <= 30));
        assertTrue(candidates.stream().anyMatch(p -> p.getNombre().equals("Arroz chaufa de pollo")));
        assertTrue(candidates.stream().noneMatch(p -> p.getTiempoPreparacion() > 30));
    }

    @Test
    void shouldLoadDishWithItsIngredients() {
        Plato chaufa = platoRepository.findById(1L).orElseThrow();

        assertEquals("Arroz chaufa de pollo", chaufa.getNombre());
        assertEquals(20, chaufa.getTiempoPreparacion());
        assertEquals(6, chaufa.getIngredientes().size());
        assertTrue(chaufa.getIngredientes().stream()
                .anyMatch(pi -> pi.getIngrediente().getNombre().equals("Arroz")));
        assertTrue(chaufa.getIngredientes().stream()
                .anyMatch(pi -> pi.getIngrediente().getNombre().equals("Pollo")));
    }
}
