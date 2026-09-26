package pe.edu.utec.manka.recommendation.scorer;

import org.springframework.stereotype.Component;
import pe.edu.utec.manka.entity.CategoriaProteina;
import pe.edu.utec.manka.entity.HistorialCocina;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.recommendation.RecommendationContext;
import pe.edu.utec.manka.recommendation.Scorer;

import java.time.LocalDateTime;

@Component
public class ProteinVarietyScorer implements Scorer {
    @Override
    public String getName() { return "proteinVariety"; }

    @Override
    public double score(Plato plato, RecommendationContext context) {
        CategoriaProteina category = plato.getCategoriaProteina();
        if (category == null) return 100.0;

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        long sameCategoryCount = context.getRecentHistory().stream()
                .filter(history -> history.getCocinadoEn().isAfter(sevenDaysAgo))
                .map(HistorialCocina::getPlato)
                .filter(previous -> previous.getCategoriaProteina() != null)
                .filter(previous -> previous.getCategoriaProteina().getId().equals(category.getId()))
                .count();

        if (sameCategoryCount == 0) return 100.0;
        if (sameCategoryCount == 1) return 70.0;
        return 30.0;
    }
}
