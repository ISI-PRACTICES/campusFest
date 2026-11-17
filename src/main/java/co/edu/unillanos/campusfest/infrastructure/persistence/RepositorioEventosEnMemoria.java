package co.edu.unillanos.campusfest.infrastructure.persistence;

import java.util.HashMap;
import java.util.Map;

import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;

/** Adaptador del puerto RepositorioEventos que guarda los eventos en un mapa. */
public class RepositorioEventosEnMemoria implements RepositorioEventos {

    private final Map<String, Evento> eventos;

    public RepositorioEventosEnMemoria() {
        this.eventos = new HashMap<>();
    }

    @Override
    public Evento buscar(String eventoId) {
        return eventos.get(eventoId);
    }

    @Override
    public void guardar(Evento evento) {
        eventos.put(evento.getId(), evento);
    }
}
