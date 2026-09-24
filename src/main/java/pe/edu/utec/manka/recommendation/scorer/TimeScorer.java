package pe.edu.utec.manka.recommendation.scorer;

import org.springframework.stereotype.Component;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.recommendation.RecommendationContext;
import pe.edu.utec.manka.recommendation.Scorer;

@Component
public class TimeScorer implements Scorer {
    @Override
    public String getName() { return "time"; }

    @Override
    public double score(Plato plato, RecommendationContext context) {
        if (plato.getTiempoPreparacion() > context.getAvailableMinutes()) {
            return 0.0;
        }
        double ratio = plato.getTiempoPreparacion() / (double) context.getAvailableMinutes();
        return 100.0 - (ratio * 20.0);
    }
}
