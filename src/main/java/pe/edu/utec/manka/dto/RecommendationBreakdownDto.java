package pe.edu.utec.manka.dto;

public class RecommendationBreakdownDto {
    private double ingredientCoverage;
    private double time;
    private double proteinVariety;
    private double repetition;
    private double popularity;

    public double getIngredientCoverage() { return ingredientCoverage; }
    public void setIngredientCoverage(double ingredientCoverage) { this.ingredientCoverage = ingredientCoverage; }
    public double getTime() { return time; }
    public void setTime(double time) { this.time = time; }
    public double getProteinVariety() { return proteinVariety; }
    public void setProteinVariety(double proteinVariety) { this.proteinVariety = proteinVariety; }
    public double getRepetition() { return repetition; }
    public void setRepetition(double repetition) { this.repetition = repetition; }
    public double getPopularity() { return popularity; }
    public void setPopularity(double popularity) { this.popularity = popularity; }
}
