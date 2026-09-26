package pe.edu.utec.manka.dto;

import pe.edu.utec.manka.entity.Dificultad;
import java.util.List;

public class RecommendationResponseDto {
    private Long platoId;
    private String nombre;
    private Integer tiempoPreparacion;
    private Dificultad dificultad;
    private double score;
    private double ingredientCoveragePercent;
    private int matchedIngredients;
    private int totalIngredients;
    private List<String> missingIngredients;
    private RecommendationBreakdownDto breakdown;
    private List<String> reasons;

    public Long getPlatoId() { return platoId; }
    public void setPlatoId(Long platoId) { this.platoId = platoId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(Integer tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }
    public Dificultad getDificultad() { return dificultad; }
    public void setDificultad(Dificultad dificultad) { this.dificultad = dificultad; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public double getIngredientCoveragePercent() { return ingredientCoveragePercent; }
    public void setIngredientCoveragePercent(double ingredientCoveragePercent) { this.ingredientCoveragePercent = ingredientCoveragePercent; }
    public int getMatchedIngredients() { return matchedIngredients; }
    public void setMatchedIngredients(int matchedIngredients) { this.matchedIngredients = matchedIngredients; }
    public int getTotalIngredients() { return totalIngredients; }
    public void setTotalIngredients(int totalIngredients) { this.totalIngredients = totalIngredients; }
    public List<String> getMissingIngredients() { return missingIngredients; }
    public void setMissingIngredients(List<String> missingIngredients) { this.missingIngredients = missingIngredients; }
    public RecommendationBreakdownDto getBreakdown() { return breakdown; }
    public void setBreakdown(RecommendationBreakdownDto breakdown) { this.breakdown = breakdown; }
    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }
}
