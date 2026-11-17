package co.edu.unillanos.campusfest.infrastructure.persistence;

import java.sql.Connection;

import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;

/** ESQUELETO (Integrante 3). */
public class RepositorioEventosH2 implements RepositorioEventos {

    public RepositorioEventosH2(Connection conexion) {
        throw new UnsupportedOperationException("Pendiente: Integrante 3");
    }

    @Override
    public Evento buscar(String eventoId) {
        throw new UnsupportedOperationException("Pendiente: Integrante 3");
    }

    @Override
    public void guardar(Evento evento) {
        throw new UnsupportedOperationException("Pendiente: Integrante 3");
    }
}
