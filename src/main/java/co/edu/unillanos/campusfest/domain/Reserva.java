package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;

public class Reserva {

    public static Reserva pendiente(String estudianteId, LocalDateTime ahora) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public static Reserva reconstruir(String codigo, String estudianteId, LocalDateTime venceEn,
                                      EstadoReserva estado, Asistencia asistencia) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public boolean ocupaCupo(LocalDateTime ahora) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public void confirmar(LocalDateTime ahora) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public void cancelar() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public Asistencia registrarIngreso(LocalDateTime ahora) {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public String getCodigo() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public String getEstudianteId() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public LocalDateTime getVenceEn() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public EstadoReserva getEstado() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }

    public Asistencia getAsistencia() {
        throw new UnsupportedOperationException("Pendiente: Integrante 1");
    }
}
