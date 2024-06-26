package alquileres.servicio;

import java.util.List;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.persistencia.dto.AlquilerDTO;
import alquileres.persistencia.dto.ReservaDTO;

public class UsuarioResumen {
    private String idUsuario;
    private List<AlquilerDTO> alquileres;
    private List<ReservaDTO> reservas;
    private boolean bloqueado;
    private int tiempoUsoHoy;
    private int tiempoUsoSemana;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<AlquilerDTO> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerDTO> alquileres) {
        this.alquileres = alquileres;
    }
    public List<ReservaDTO> getReservas() {
        return reservas;
    }
    public void setReservas(List<ReservaDTO> reservas) {
        this.reservas = reservas;
    }
    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public int getTiempoUsoHoy() {
        return tiempoUsoHoy;
    }

    public void setTiempoUsoHoy(int tiempoUsoHoy) {
        this.tiempoUsoHoy = tiempoUsoHoy;
    }

    public int getTiempoUsoSemana() {
        return tiempoUsoSemana;
    }

    public void setTiempoUsoSemana(int tiempoUsoSemana) {
        this.tiempoUsoSemana = tiempoUsoSemana;
    }

}
