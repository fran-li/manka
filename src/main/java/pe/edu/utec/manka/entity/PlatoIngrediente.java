package pe.edu.utec.manka.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "dish_ingredients",
        uniqueConstraints = @UniqueConstraint(columnNames = {"dish_id", "ingredient_id"})
)
public class PlatoIngrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dish_id", nullable = false)
    private Plato plato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingrediente ingrediente;

    @Column(name = "quantity_text")
    private String cantidadTexto;

    @Column(name = "is_required", nullable = false)
    private Boolean obligatorio = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Plato getPlato() { return plato; }
    public void setPlato(Plato plato) { this.plato = plato; }
    public Ingrediente getIngrediente() { return ingrediente; }
    public void setIngrediente(Ingrediente ingrediente) { this.ingrediente = ingrediente; }
    public String getCantidadTexto() { return cantidadTexto; }
    public void setCantidadTexto(String cantidadTexto) { this.cantidadTexto = cantidadTexto; }
    public Boolean getObligatorio() { return obligatorio; }
    public void setObligatorio(Boolean obligatorio) { this.obligatorio = obligatorio; }
}
