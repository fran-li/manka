package pe.edu.utec.manka.recommendation;

import pe.edu.utec.manka.entity.Plato;

public interface Scorer {
    String getName();
    double score(Plato plato, RecommendationContext context);
}
