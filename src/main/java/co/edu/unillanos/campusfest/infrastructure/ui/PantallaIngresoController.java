package co.edu.unillanos.campusfest.infrastructure.ui;

import java.time.format.DateTimeFormatter;

import co.edu.unillanos.campusfest.application.usecase.RegistrarIngreso;
import co.edu.unillanos.campusfest.domain.Asistencia;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Pantalla del CU4: recibe el código del QR como texto (escrito o escaneado)
 * y llama a RegistrarIngreso.
 */
public class PantallaIngresoController {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final RegistrarIngreso registrarIngreso;

    @FXML
    private TextField campoEventoId;

    @FXML
    private TextField campoCodigo;

    @FXML
    private Label etiquetaMensaje;

    public PantallaIngresoController(RegistrarIngreso registrarIngreso) {
        this.registrarIngreso = registrarIngreso;
    }

    @FXML
    private void alRegistrar() {
        String eventoId = campoEventoId.getText().trim();
        String codigo = campoCodigo.getText().trim();
        if (eventoId.isEmpty() || codigo.isEmpty()) {
            mostrarError("Escriba el evento y escanee el código QR.");
            return;
        }
        try {
            Asistencia asistencia = registrarIngreso.registrar(eventoId, codigo);
            campoCodigo.clear();
            mostrarExito("Ingreso registrado a las " + asistencia.getHora().format(FORMATO_HORA) + ".");
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
