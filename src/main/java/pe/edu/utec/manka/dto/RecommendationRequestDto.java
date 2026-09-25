package pe.edu.utec.manka.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class RecommendationRequestDto {
    private Set<Long> ingredientIds;

    @NotNull
    @Min(1)
    @Max(240)
    private Integer availableMinutes;

    @Min(1)
    @Max(20)
    private Integer limit = 10;

    public Set<Long> getIngredientIds() { return ingredientIds; }
    public void setIngredientIds(Set<Long> ingredientIds) { this.ingredientIds = ingredientIds; }
    public Integer getAvailableMinutes() { return availableMinutes; }
    public void setAvailableMinutes(Integer availableMinutes) { this.availableMinutes = availableMinutes; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
}
