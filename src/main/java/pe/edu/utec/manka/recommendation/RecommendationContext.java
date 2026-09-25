package pe.edu.utec.manka.recommendation;

import pe.edu.utec.manka.entity.HistorialCocina;
import pe.edu.utec.manka.entity.Ingrediente;

import java.util.List;
import java.util.Set;

public class RecommendationContext {
    private final Set<Ingrediente> availableIngredients;
    private final Integer availableMinutes;
    private final List<HistorialCocina> recentHistory;

    public RecommendationContext(Set<Ingrediente> availableIngredients,
                                 Integer availableMinutes,
                                 List<HistorialCocina> recentHistory) {
        this.availableIngredients = availableIngredients;
        this.availableMinutes = availableMinutes;
        this.recentHistory = recentHistory;
    }

    public Set<Ingrediente> getAvailableIngredients() { return availableIngredients; }
    public Integer getAvailableMinutes() { return availableMinutes; }
    public List<HistorialCocina> getRecentHistory() { return recentHistory; }
}
