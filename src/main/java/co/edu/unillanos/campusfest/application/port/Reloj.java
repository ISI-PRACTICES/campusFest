package co.edu.unillanos.campusfest.application.port;

import java.time.LocalDateTime;

/** Puerto de la hora: los casos de uso piden la hora aquí, nunca a now(). */
public interface Reloj {

    LocalDateTime ahora();
}
