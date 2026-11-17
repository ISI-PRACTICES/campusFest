package co.edu.unillanos.campusfest.infrastructure.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import co.edu.unillanos.campusfest.application.usecase.EditarEvento;
import co.edu.unillanos.campusfest.domain.Evento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** Pantalla del CU3: carga el evento en el formulario y guarda los cambios. */
public class PantallaEventoController {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final EditarEvento editarEvento;

    @FXML
    private TextField campoEventoId;

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoFecha;

    @FXML
    private TextField campoLugar;

    @FXML
    private TextField campoAforo;

    @FXML
    private Label etiquetaMensaje;

    public PantallaEventoController(EditarEvento editarEvento) {
        this.editarEvento = editarEvento;
    }

    @FXML
    private void alCargar() {
        String eventoId = campoEventoId.getText().trim();
        if (eventoId.isEmpty()) {
            mostrarError("Escriba el identificador del evento.");
            return;
        }
        try {
            Evento evento = editarEvento.obtener(eventoId);
            campoNombre.setText(evento.getNombre());
            campoFecha.setText(evento.getFecha().format(FORMATO_FECHA));
            campoLugar.setText(evento.getLugar());
            campoAforo.setText(String.valueOf(evento.getAforo()));
            mostrarExito("Evento cargado. Cambie los datos y guarde.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void alGuardar() {
        String eventoId = campoEventoId.getText().trim();
        if (eventoId.isEmpty()) {
            mostrarError("Escriba el identificador del evento.");
            return;
        }
        try {
            LocalDateTime fecha = LocalDateTime.parse(campoFecha.getText().trim(), FORMATO_FECHA);
            int aforo = Integer.parseInt(campoAforo.getText().trim());
            editarEvento.editar(eventoId, campoNombre.getText(), fecha, campoLugar.getText(), aforo);
            mostrarExito("Evento actualizado.");
        } catch (DateTimeParseException e) {
            mostrarError("La fecha debe tener el formato aaaa-mm-dd hh:mm, por ejemplo 2026-11-28 19:00.");
        } catch (NumberFormatException e) {
            mostrarError("El aforo debe ser un número entero.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarExito(String texto) {
        etiquetaMensaje.getStyleClass().setAll("label", "mensaje", "mensaje-exito");
        etiquetaMensaje.setText(texto);
    }

    private void mostrarError(String texto) {
        etiquetaMensaje.getStyleClass().setAll("label", "mensaje", "mensaje-error");
        etiquetaMensaje.setText(texto);
    }
}
