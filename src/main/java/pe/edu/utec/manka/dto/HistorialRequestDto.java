package pe.edu.utec.manka.dto;

import jakarta.validation.constraints.NotNull;

public class HistorialRequestDto {
    @NotNull
    private Long dishId;

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
}
