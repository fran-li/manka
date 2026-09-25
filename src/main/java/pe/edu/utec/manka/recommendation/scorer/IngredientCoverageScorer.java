package pe.edu.utec.manka.recommendation.scorer;

import org.springframework.stereotype.Component;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.entity.PlatoIngrediente;
import pe.edu.utec.manka.recommendation.IngredientMatching;
import pe.edu.utec.manka.recommendation.RecommendationContext;
import pe.edu.utec.manka.recommendation.Scorer;

import java.util.List;

@Component
public class IngredientCoverageScorer implements Scorer {
    @Override
    public String getName() { return "ingredientCoverage"; }

    @Override
    public double score(Plato plato, RecommendationContext context) {
        List<PlatoIngrediente> required = IngredientMatching.requiredIngredients(plato.getIngredientes());
        if (required.isEmpty()) return 100.0;

        long matched = required.stream()
                .filter(pi -> IngredientMatching.hasIngredient(pi.getIngrediente(), context.getAvailableIngredients()))
                .count();

        return matched * 100.0 / required.size();
    }
}
