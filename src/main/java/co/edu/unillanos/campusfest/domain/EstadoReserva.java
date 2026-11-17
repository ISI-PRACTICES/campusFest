package co.edu.unillanos.campusfest.domain;

/**
 * Estados de una reserva.
 * PENDIENTE pasa a CONFIRMADA o VENCIDA; PENDIENTE o CONFIRMADA pasan a CANCELADA.
 */
public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    VENCIDA,
    CANCELADA
}
