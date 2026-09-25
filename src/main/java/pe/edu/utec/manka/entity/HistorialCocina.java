package pe.edu.utec.manka.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cooking_history")
public class HistorialCocina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dish_id", nullable = false)
    private Plato plato;

    @Column(name = "cooked_at", nullable = false)
    private LocalDateTime cocinadoEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Plato getPlato() { return plato; }
    public void setPlato(Plato plato) { this.plato = plato; }
    public LocalDateTime getCocinadoEn() { return cocinadoEn; }
    public void setCocinadoEn(LocalDateTime cocinadoEn) { this.cocinadoEn = cocinadoEn; }
}
