package pe.edu.utec.manka.recommendation.scorer;

import org.springframework.stereotype.Component;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.recommendation.RecommendationContext;
import pe.edu.utec.manka.recommendation.Scorer;

@Component
public class PopularityScorer implements Scorer {
    @Override
    public String getName() { return "popularity"; }

    @Override
    public double score(Plato plato, RecommendationContext context) {
        return plato.getPopularidad();
    }
}
