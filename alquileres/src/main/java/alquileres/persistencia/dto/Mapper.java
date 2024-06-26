package alquileres.persistencia.dto;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;

public class Mapper {

    public static AlquilerDTO toAlquilerDTO(Alquiler alquiler) {
        if (alquiler == null) {
            return null;
        }
        AlquilerDTO alquilerDTO = new AlquilerDTO();
        alquilerDTO.setInicio(alquiler.getInicio() != null ? alquiler.getInicio().toString() : null);
        alquilerDTO.setFin(alquiler.getFin() != null ? alquiler.getFin().toString() : null);
        alquilerDTO.setIdBicicleta(alquiler.getId());
        alquilerDTO.setActivo(alquiler.isActivo());
        alquilerDTO.setTiempo(alquiler.getTiempo());
        return alquilerDTO;
    }

    public static ReservaDTO toReservaDTO(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setIdBicicleta(reserva.getIdBicicleta());
        reservaDTO.setCreada(reserva.getCreada() != null ? reserva.getCreada().toString() : null);
        reservaDTO.setCaducidad(reserva.getCaducidad() != null ? reserva.getCaducidad().toString() : null);
        reservaDTO.setActiva(reserva.isActiva());
        reservaDTO.setCaducada(reserva.isCaducada());
        return reservaDTO;
    }


}
