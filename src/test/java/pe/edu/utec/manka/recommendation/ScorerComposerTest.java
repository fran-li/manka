package pe.edu.utec.manka.recommendation;

import org.junit.jupiter.api.Test;
import pe.edu.utec.manka.entity.Plato;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScorerComposerTest {

    @Test
    void shouldRejectWeightsThatDoNotSumOne() {
        Scorer scorer = new FixedScorer("a", 100);
        assertThrows(IllegalArgumentException.class,
                () -> new ScorerComposer(List.of(new WeightedScorer(scorer, 0.5))));
    }

    @Test
    void shouldCalculateWeightedScore() {
        ScorerComposer composer = new ScorerComposer(List.of(
                new WeightedScorer(new FixedScorer("a", 100), 0.6),
                new WeightedScorer(new FixedScorer("b", 50), 0.4)
        ));

        ScoringResult result = composer.calculate(new Plato(), null);
        assertEquals(80.0, result.getFinalScore());
    }

    private static class FixedScorer implements Scorer {
        private final String name;
        private final double score;

        private FixedScorer(String name, double score) {
            this.name = name;
            this.score = score;
        }

        @Override public String getName() { return name; }
        @Override public double score(Plato plato, RecommendationContext context) { return score; }
    }
}
