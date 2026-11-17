package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Evento de CampusFest. Es el experto en su aforo y en sus cupos, y el creador
 * de sus reservas.
 */
public class Evento {

    private final String id;
    private String nombre;
    private LocalDateTime fecha;
    private String lugar;
    private int aforo;
    private final int bloqueados;
    private final List<Reserva> reservas;

    public Evento(String id, String nombre, LocalDateTime fecha, String lugar,
                  int aforo, int bloqueados) {
        this(id, nombre, fecha, lugar, aforo, bloqueados, new ArrayList<Reserva>());
    }

    /** Usado por el adaptador H2 para reconstruir el evento con sus reservas. */
    public Evento(String id, String nombre, LocalDateTime fecha, String lugar,
                  int aforo, int bloqueados, List<Reserva> reservas) {
        validarTexto(id, "El identificador del evento");
        validarDatos(nombre, fecha, lugar);
        if (bloqueados < 0) {
            throw new IllegalArgumentException("Los cupos bloqueados no pueden ser negativos.");
        }
        if (aforo < bloqueados) {
            throw new IllegalArgumentException("El aforo no puede ser menor que los cupos bloqueados.");
        }
        if (reservas == null) {
            throw new IllegalArgumentException("La lista de reservas es obligatoria.");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.fecha = fecha;
        this.lugar = lugar.trim();
        this.aforo = aforo;
        this.bloqueados = bloqueados;
        this.reservas = new ArrayList<>(reservas);
    }

    // ---------- CU1 · Consultar cupos ----------

    /** Cupos disponibles = aforo − bloqueados − cupos ocupados por reservas. */
    public int cuposDisponibles(LocalDateTime ahora) {
        return aforo - aforoMinimo(ahora);
    }

    // ---------- CU2 · Reservar ----------

    public Reserva reservar(String estudianteId, LocalDateTime ahora) {
        if (cuposDisponibles(ahora) <= 0) {
            throw new IllegalStateException("No hay cupos disponibles para «" + nombre + "».");
        }
        Reserva reserva = Reserva.pendiente(estudianteId, ahora);
        reservas.add(reserva);
        return reserva;
    }

    public Reserva buscarReservaPorCodigo(String codigo) {
        for (Reserva reserva : reservas) {
            if (reserva.getCodigo().equals(codigo)) {
                return reserva;
            }
        }
        throw new IllegalArgumentException("El código «" + codigo
                + "» no corresponde a ninguna reserva de este evento.");
    }

    // ---------- CU3 · Editar evento ----------

    public void cambiarDatos(String nombre, LocalDateTime fecha, String lugar) {
        validarDatos(nombre, fecha, lugar);
        this.nombre = nombre.trim();
        this.fecha = fecha;
        this.lugar = lugar.trim();
    }

    /** El aforo no puede quedar por debajo de bloqueados + cupos ocupados por reservas. */
    public void cambiarAforo(int nuevoAforo, LocalDateTime ahora) {
        int ocupados = cuposOcupadosPorReservas(ahora);
        int minimo = bloqueados + ocupados;
        if (nuevoAforo < minimo) {
            throw new IllegalStateException("Hay " + ocupados + " cupos ocupados por reservas y "
                    + bloqueados + " bloqueados: el aforo no puede ser menor que " + minimo + ".");
        }
        this.aforo = nuevoAforo;
    }

    // ---------- Reglas internas (un solo lugar) ----------

    private int cuposOcupadosPorReservas(LocalDateTime ahora) {
        int ocupados = 0;
        for (Reserva reserva : reservas) {
            if (reserva.ocupaCupo(ahora)) {
                ocupados++;
            }
        }
        return ocupados;
    }

    private int aforoMinimo(LocalDateTime ahora) {
        return bloqueados + cuposOcupadosPorReservas(ahora);
    }

    private static void validarDatos(String nombre, LocalDateTime fecha, String lugar) {
        validarTexto(nombre, "El nombre del evento");
        validarTexto(lugar, "El lugar del evento");
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del evento es obligatoria.");
        }
    }

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
    }

    // ---------- Consulta ----------

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public int getAforo() {
        return aforo;
    }

    public int getBloqueados() {
        return bloqueados;
    }

    /** Lista de solo lectura. */
    public List<Reserva> getReservas() {
        return Collections.unmodifiableList(reservas);
    }
}
