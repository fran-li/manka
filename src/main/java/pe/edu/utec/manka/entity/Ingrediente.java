package pe.edu.utec.manka.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredients", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Ingrediente parent;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Ingrediente getParent() { return parent; }
    public void setParent(Ingrediente parent) { this.parent = parent; }
}
