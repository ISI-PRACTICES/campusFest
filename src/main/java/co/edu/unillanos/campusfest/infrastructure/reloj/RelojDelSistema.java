package co.edu.unillanos.campusfest.infrastructure.reloj;

import java.time.LocalDateTime;

import co.edu.unillanos.campusfest.application.port.Reloj;

/** Adaptador del puerto Reloj. Es la única clase que lee la hora del sistema. */
public class RelojDelSistema implements Reloj {

    @Override
    public LocalDateTime ahora() {
        return LocalDateTime.now();
    }
}
