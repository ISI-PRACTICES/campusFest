package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Reserva de un estudiante para un evento.
 * Es la experta en su plazo de 10 minutos, en su estado y en su ingreso.
 */
public class Reserva {

    private static final int MINUTOS_PARA_CONFIRMAR = 10;

    private final String codigo;
    private final String estudianteId;
    private final LocalDateTime venceEn;
    private EstadoReserva estado;
    private Asistencia asistencia;

    private Reserva(String codigo, String estudianteId, LocalDateTime venceEn,
                    EstadoReserva estado, Asistencia asistencia) {
        this.codigo = codigo;
        this.estudianteId = estudianteId;
        this.venceEn = venceEn;
        this.estado = estado;
        this.asistencia = asistencia;
    }

    /** Crea una reserva PENDIENTE que vence 10 minutos después de ahora. */
    public static Reserva pendiente(String estudianteId, LocalDateTime ahora) {
        if (estudianteId == null || estudianteId.isBlank()) {
            throw new IllegalArgumentException("El código del estudiante es obligatorio.");
        }
        if (ahora == null) {
            throw new IllegalArgumentException("La hora de la reserva es obligatoria.");
        }
        String codigo = UUID.randomUUID().toString();
        return new Reserva(codigo, estudianteId.trim(), ahora.plusMinutes(MINUTOS_PARA_CONFIRMAR),
                EstadoReserva.PENDIENTE, null);
    }

    /** Vuelve a armar una reserva leída de la base de datos. No aplica reglas. */
    public static Reserva reconstruir(String codigo, String estudianteId, LocalDateTime venceEn,
                                      EstadoReserva estado, Asistencia asistencia) {
        return new Reserva(codigo, estudianteId, venceEn, estado, asistencia);
    }

    /** Una reserva ocupa cupo si está confirmada o si está pendiente y no ha vencido. */
    public boolean ocupaCupo(LocalDateTime ahora) {
        if (estado == EstadoReserva.CONFIRMADA) {
            return true;
        }
        return estado == EstadoReserva.PENDIENTE && estaDentroDelPlazo(ahora);
    }

    public void confirmar(LocalDateTime ahora) {
        if (estado != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("La reserva no se puede confirmar porque está "
                    + nombreDelEstado() + ".");
        }
        if (!estaDentroDelPlazo(ahora)) {
            estado = EstadoReserva.VENCIDA;
            throw new IllegalStateException("La reserva venció: había que confirmarla en "
                    + MINUTOS_PARA_CONFIRMAR + " minutos.");
        }
        estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar() {
        if (estado != EstadoReserva.PENDIENTE && estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("La reserva no se puede cancelar porque está "
                    + nombreDelEstado() + ".");
        }
        estado = EstadoReserva.CANCELADA;
    }

    /** Registra el único ingreso permitido. Solo una reserva confirmada puede ingresar. */
    public Asistencia registrarIngreso(LocalDateTime ahora) {
        if (estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo puede ingresar una reserva confirmada; esta está "
                    + nombreDelEstado() + ".");
        }
        if (asistencia != null) {
            throw new IllegalStateException("Esta reserva ya registró su ingreso a las "
                    + asistencia.getHora() + ".");
        }
        asistencia = new Asistencia(ahora);
        return asistencia;
    }

    private boolean estaDentroDelPlazo(LocalDateTime ahora) {
        return ahora.isBefore(venceEn);
    }

    private String nombreDelEstado() {
        return estado.name().toLowerCase();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public LocalDateTime getVenceEn() {
        return venceEn;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    /** Devuelve null si la reserva no ha ingresado. */
    public Asistencia getAsistencia() {
        return asistencia;
    }
}
