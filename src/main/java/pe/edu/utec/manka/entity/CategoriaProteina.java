package pe.edu.utec.manka.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "protein_categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class CategoriaProteina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String nombre;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
