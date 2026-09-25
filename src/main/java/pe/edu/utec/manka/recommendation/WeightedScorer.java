package pe.edu.utec.manka.recommendation;

public class WeightedScorer {
    private final Scorer scorer;
    private final double weight;

    public WeightedScorer(Scorer scorer, double weight) {
        this.scorer = scorer;
        this.weight = weight;
    }

    public Scorer getScorer() { return scorer; }
    public double getWeight() { return weight; }
}
