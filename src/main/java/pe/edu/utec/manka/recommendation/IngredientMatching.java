package pe.edu.utec.manka.recommendation;

import pe.edu.utec.manka.entity.Ingrediente;
import pe.edu.utec.manka.entity.PlatoIngrediente;

import java.util.List;
import java.util.Set;

public final class IngredientMatching {
    private IngredientMatching() {}

    public static boolean matches(Ingrediente required, Ingrediente available) {
        if (required.getId().equals(available.getId())) {
            return true;
        }
        if (available.getParent() != null && available.getParent().getId().equals(required.getId())) {
            return true;
        }
        return required.getParent() != null && required.getParent().getId().equals(available.getId());
    }

    public static boolean hasIngredient(Ingrediente required, Set<Ingrediente> availableIngredients) {
        return availableIngredients.stream().anyMatch(available -> matches(required, available));
    }

    public static List<PlatoIngrediente> requiredIngredients(List<PlatoIngrediente> ingredients) {
        return ingredients.stream()
                .filter(pi -> Boolean.TRUE.equals(pi.getObligatorio()))
                .toList();
    }
}
