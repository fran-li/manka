package pe.edu.utec.manka.recommendation.scorer;

import org.springframework.stereotype.Component;
import pe.edu.utec.manka.entity.HistorialCocina;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.recommendation.RecommendationContext;
import pe.edu.utec.manka.recommendation.Scorer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class RepetitionScorer implements Scorer {
    @Override
    public String getName() { return "repetition"; }

    @Override
    public double score(Plato plato, RecommendationContext context) {
        Optional<HistorialCocina> mostRecent = context.getRecentHistory().stream()
                .filter(history -> history.getPlato().getId().equals(plato.getId()))
                .max((a, b) -> a.getCocinadoEn().compareTo(b.getCocinadoEn()));

        if (mostRecent.isEmpty()) return 100.0;

        long days = Duration.between(mostRecent.get().getCocinadoEn(), LocalDateTime.now()).toDays();
        if (days <= 7) return 0.0;
        if (days <= 14) return 40.0;
        return 70.0;
    }
}
