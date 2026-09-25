package pe.edu.utec.manka.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class DespensaRequestDto {
    @NotNull
    private Set<Long> ingredientIds;

    public Set<Long> getIngredientIds() { return ingredientIds; }
    public void setIngredientIds(Set<Long> ingredientIds) { this.ingredientIds = ingredientIds; }
}
