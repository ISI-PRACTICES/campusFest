package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Evento {

    public Evento(String id, String nombre, LocalDateTime fecha, String lugar,
                  int aforo, int bloqueados) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public Evento(String id, String nombre, LocalDateTime fecha, String lugar,
                  int aforo, int bloqueados, List<Reserva> reservas) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public int cuposDisponibles(LocalDateTime ahora) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public Reserva reservar(String estudianteId, LocalDateTime ahora) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public Reserva buscarReservaPorCodigo(String codigo) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public void cambiarDatos(String nombre, LocalDateTime fecha, String lugar) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public void cambiarAforo(int nuevoAforo, LocalDateTime ahora) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public String getId() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public String getNombre() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public LocalDateTime getFecha() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public String getLugar() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public int getAforo() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public int getBloqueados() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public List<Reserva> getReservas() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }
}
