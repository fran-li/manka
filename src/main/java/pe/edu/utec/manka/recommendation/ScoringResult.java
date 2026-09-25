package pe.edu.utec.manka.recommendation;

import java.util.Map;

public class ScoringResult {
    private final double finalScore;
    private final Map<String, Double> breakdown;

    public ScoringResult(double finalScore, Map<String, Double> breakdown) {
        this.finalScore = finalScore;
        this.breakdown = breakdown;
    }

    public double getFinalScore() { return finalScore; }
    public Map<String, Double> getBreakdown() { return breakdown; }
}
