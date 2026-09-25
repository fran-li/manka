package pe.edu.utec.manka.dto;

import java.time.LocalDateTime;

public class HistorialResponseDto {
    private Long id;
    private Long dishId;
    private String dishName;
    private LocalDateTime cookedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public LocalDateTime getCookedAt() { return cookedAt; }
    public void setCookedAt(LocalDateTime cookedAt) { this.cookedAt = cookedAt; }
}
