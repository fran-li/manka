package pe.edu.utec.manka.recommendation;

import org.junit.jupiter.api.Test;
import pe.edu.utec.manka.entity.Ingrediente;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.entity.PlatoIngrediente;
import pe.edu.utec.manka.recommendation.scorer.IngredientCoverageScorer;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientCoverageScorerTest {

    @Test
    void shouldCalculateCoverageFromAvailableIngredients() {
        Ingrediente pollo = ingredient(1L, "Pollo");
        Ingrediente arroz = ingredient(2L, "Arroz");
        Ingrediente huevo = ingredient(3L, "Huevo");

        Plato dish = new Plato();
        dish.setIngredientes(List.of(link(pollo), link(arroz), link(huevo)));

        RecommendationContext context = new RecommendationContext(Set.of(pollo, arroz), 30, List.of());
        double score = new IngredientCoverageScorer().score(dish, context);

        assertEquals(66.666, score, 0.01);
    }

    @Test
    void childVariantShouldMatchParentIngredient() {
        Ingrediente pollo = ingredient(1L, "Pollo");
        Ingrediente pechuga = ingredient(2L, "Pechuga de pollo");
        pechuga.setParent(pollo);

        Plato dish = new Plato();
        dish.setIngredientes(List.of(link(pollo)));

        RecommendationContext context = new RecommendationContext(Set.of(pechuga), 30, List.of());
        double score = new IngredientCoverageScorer().score(dish, context);

        assertEquals(100.0, score);
    }

    private Ingrediente ingredient(Long id, String name) {
        Ingrediente ingredient = new Ingrediente();
        ingredient.setId(id);
        ingredient.setNombre(name);
        return ingredient;
    }

    private PlatoIngrediente link(Ingrediente ingredient) {
        PlatoIngrediente item = new PlatoIngrediente();
        item.setIngrediente(ingredient);
        item.setObligatorio(true);
        return item;
    }
}
