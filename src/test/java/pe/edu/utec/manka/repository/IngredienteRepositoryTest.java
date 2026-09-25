package pe.edu.utec.manka.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import pe.edu.utec.manka.AbstractContainerBaseTest;
import pe.edu.utec.manka.entity.Ingrediente;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IngredienteRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Test
    void shouldSearchIngredientsIgnoringCase() {
        List<Ingrediente> results =
                ingredienteRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc("POL");

        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(i -> i.getNombre().equals("Pollo")));
        assertTrue(results.stream().anyMatch(i -> i.getNombre().equals("Pechuga de pollo")));
        assertTrue(results.stream().anyMatch(i -> i.getNombre().equals("Pierna de pollo")));
        assertTrue(results.stream().anyMatch(i -> i.getNombre().equals("Caldo de pollo")));
    }

    @Test
    void shouldLoadParentIngredientRelation() {
        Ingrediente pechuga = ingredienteRepository
                .findByNombreContainingIgnoreCaseOrderByNombreAsc("Pechuga de pollo")
                .stream()
                .filter(i -> i.getNombre().equals("Pechuga de pollo"))
                .findFirst()
                .orElseThrow();

        assertEquals("Pollo", pechuga.getParent().getNombre());
        assertEquals(1L, pechuga.getParent().getId());
    }
}
