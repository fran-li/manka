package pe.edu.utec.manka.recommendation;

import pe.edu.utec.manka.entity.Plato;
import java.util.List;

public class RecommendationResult {
    private final Plato plato;
    private final ScoringResult scoring;
    private final int matchedIngredients;
    private final int totalIngredients;
    private final List<String> missingIngredients;

    public RecommendationResult(Plato plato,
                                ScoringResult scoring,
                                int matchedIngredients,
                                int totalIngredients,
                                List<String> missingIngredients) {
        this.plato = plato;
        this.scoring = scoring;
        this.matchedIngredients = matchedIngredients;
        this.totalIngredients = totalIngredients;
        this.missingIngredients = missingIngredients;
    }

    public Plato getPlato() { return plato; }
    public ScoringResult getScoring() { return scoring; }
    public int getMatchedIngredients() { return matchedIngredients; }
    public int getTotalIngredients() { return totalIngredients; }
    public List<String> getMissingIngredients() { return missingIngredients; }

    public double getCoveragePercent() {
        if (totalIngredients == 0) return 100.0;
        return Math.round((matchedIngredients * 10000.0 / totalIngredients)) / 100.0;
    }
}
