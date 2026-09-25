package pe.edu.utec.manka.dto;

import pe.edu.utec.manka.entity.Dificultad;
import java.util.List;

public class PlatoResponseDto {
    private Long id;
    private String nombre;
    private Integer tiempoPreparacion;
    private Dificultad dificultad;
    private Integer popularidad;
    private String categoriaProteina;
    private String preparacion;
    private List<PlatoIngredienteResponseDto> ingredientes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(Integer tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }
    public Dificultad getDificultad() { return dificultad; }
    public void setDificultad(Dificultad dificultad) { this.dificultad = dificultad; }
    public Integer getPopularidad() { return popularidad; }
    public void setPopularidad(Integer popularidad) { this.popularidad = popularidad; }
    public String getCategoriaProteina() { return categoriaProteina; }
    public void setCategoriaProteina(String categoriaProteina) { this.categoriaProteina = categoriaProteina; }
    public String getPreparacion() { return preparacion; }
    public void setPreparacion(String preparacion) { this.preparacion = preparacion; }
    public List<PlatoIngredienteResponseDto> getIngredientes() { return ingredientes; }
    public void setIngredientes(List<PlatoIngredienteResponseDto> ingredientes) { this.ingredientes = ingredientes; }
}
