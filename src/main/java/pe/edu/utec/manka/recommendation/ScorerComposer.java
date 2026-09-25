package pe.edu.utec.manka.recommendation;

import pe.edu.utec.manka.entity.Plato;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScorerComposer {
    private final List<WeightedScorer> scorers;

    public ScorerComposer(List<WeightedScorer> scorers) {
        double totalWeight = scorers.stream().mapToDouble(WeightedScorer::getWeight).sum();
        if (Math.abs(totalWeight - 1.0) > 0.000001) {
            throw new IllegalArgumentException("Los pesos de los scorers deben sumar 1.0. Suma actual: " + totalWeight);
        }
        this.scorers = scorers;
    }

    public ScoringResult calculate(Plato plato, RecommendationContext context) {
        Map<String, Double> breakdown = new LinkedHashMap<>();
        double finalScore = 0.0;

        for (WeightedScorer weightedScorer : scorers) {
            double score = clamp(weightedScorer.getScorer().score(plato, context));
            breakdown.put(weightedScorer.getScorer().getName(), round(score));
            finalScore += score * weightedScorer.getWeight();
        }

        return new ScoringResult(round(finalScore), breakdown);
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(100.0, value));
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
