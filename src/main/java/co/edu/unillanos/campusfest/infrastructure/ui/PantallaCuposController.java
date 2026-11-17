package co.edu.unillanos.campusfest.infrastructure.ui;

import co.edu.unillanos.campusfest.application.usecase.ConsultarCupos;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** Pantalla del CU1: lee el evento, llama a ConsultarCupos y muestra el resultado. */
public class PantallaCuposController {

    private final ConsultarCupos consultarCupos;

    @FXML
    private TextField campoEventoId;

    @FXML
    private Label etiquetaMensaje;

    public PantallaCuposController(ConsultarCupos consultarCupos) {
        this.consultarCupos = consultarCupos;
    }

    @FXML
    private void alConsultar() {
        String eventoId = campoEventoId.getText().trim();
        if (eventoId.isEmpty()) {
            mostrarError("Escriba el identificador del evento.");
            return;
        }
        try {
            int cupos = consultarCupos.consultar(eventoId);
            mostrarExito("Cupos disponibles: " + cupos);
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
