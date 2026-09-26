package pe.edu.utec.manka.dto;

public class IngredienteResponseDto {
    private Long id;
    private String nombre;
    private Long parentId;
    private String parentNombre;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getParentNombre() { return parentNombre; }
    public void setParentNombre(String parentNombre) { this.parentNombre = parentNombre; }
}
