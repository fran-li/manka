package pe.edu.utec.manka.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dishes", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String nombre;

    @Column(name = "preparation_minutes", nullable = false)
    private Integer tiempoPreparacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Dificultad dificultad;

    @Column(name = "popularity_score", nullable = false)
    private Integer popularidad;

    @Column(name = "preparation", columnDefinition = "TEXT", nullable = false)
    private String preparacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "protein_category_id")
    private CategoriaProteina categoriaProteina;

    @OneToMany(mappedBy = "plato", fetch = FetchType.LAZY)
    private List<PlatoIngrediente> ingredientes = new ArrayList<>();

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
    public String getPreparacion() { return preparacion; }
    public void setPreparacion(String preparacion) { this.preparacion = preparacion; }
    public CategoriaProteina getCategoriaProteina() { return categoriaProteina; }
    public void setCategoriaProteina(CategoriaProteina categoriaProteina) { this.categoriaProteina = categoriaProteina; }
    public List<PlatoIngrediente> getIngredientes() { return ingredientes; }
    public void setIngredientes(List<PlatoIngrediente> ingredientes) { this.ingredientes = ingredientes; }
}
