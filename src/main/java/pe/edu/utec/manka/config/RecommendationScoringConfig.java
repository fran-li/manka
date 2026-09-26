package pe.edu.utec.manka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.utec.manka.recommendation.ScorerComposer;
import pe.edu.utec.manka.recommendation.WeightedScorer;
import pe.edu.utec.manka.recommendation.scorer.IngredientCoverageScorer;
import pe.edu.utec.manka.recommendation.scorer.PopularityScorer;
import pe.edu.utec.manka.recommendation.scorer.ProteinVarietyScorer;
import pe.edu.utec.manka.recommendation.scorer.RepetitionScorer;
import pe.edu.utec.manka.recommendation.scorer.TimeScorer;

import java.util.List;

@Configuration
public class RecommendationScoringConfig {

    @Bean
    public ScorerComposer scorerComposer(
            IngredientCoverageScorer ingredientCoverageScorer,
            TimeScorer timeScorer,
            ProteinVarietyScorer proteinVarietyScorer,
            RepetitionScorer repetitionScorer,
            PopularityScorer popularityScorer,
            @Value("${manka.scoring.ingredient-coverage}") double ingredientCoverageWeight,
            @Value("${manka.scoring.time}") double timeWeight,
            @Value("${manka.scoring.protein-variety}") double proteinVarietyWeight,
            @Value("${manka.scoring.repetition}") double repetitionWeight,
            @Value("${manka.scoring.popularity}") double popularityWeight) {

        return new ScorerComposer(List.of(
                new WeightedScorer(ingredientCoverageScorer, ingredientCoverageWeight),
                new WeightedScorer(timeScorer, timeWeight),
                new WeightedScorer(proteinVarietyScorer, proteinVarietyWeight),
                new WeightedScorer(repetitionScorer, repetitionWeight),
                new WeightedScorer(popularityScorer, popularityWeight)
        ));
    }
}
