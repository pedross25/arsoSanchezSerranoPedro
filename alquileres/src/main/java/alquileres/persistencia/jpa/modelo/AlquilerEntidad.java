package alquileres.persistencia.jpa.modelo;

import repositorio.Identificable;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "alquileres")
public class AlquilerEntidad implements Identificable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String idBicicleta;

    @Column(nullable = false)
    private LocalDateTime inicio;

    @Column
    private LocalDateTime fin;

    @Column
    private String usuarioId;

    // Getters y setters

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdBicicleta() {
        return idBicicleta;
    }

    public void setIdBicicleta(String idBicicleta) {
        this.idBicicleta = idBicicleta;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String getId() {
        return id.toString();
    }

    @Override
    public void setId(String id) {
        this.id = Long.valueOf(id);
    }
}
