package co.edu.unillanos.campusfest.infrastructure.ui;

import java.time.format.DateTimeFormatter;

import co.edu.unillanos.campusfest.application.usecase.ReservarEntrada;
import co.edu.unillanos.campusfest.domain.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** Pantalla del CU2: solicitar una reserva y confirmarla con su código. */
public class PantallaReservaController {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ReservarEntrada reservarEntrada;

    @FXML
    private TextField campoEventoId;

    @FXML
    private TextField campoEstudianteId;

    @FXML
    private TextField campoCodigo;

    @FXML
    private Label etiquetaMensaje;

    public PantallaReservaController(ReservarEntrada reservarEntrada) {
        this.reservarEntrada = reservarEntrada;
    }

    @FXML
    private void alSolicitar() {
        String eventoId = campoEventoId.getText().trim();
        String estudianteId = campoEstudianteId.getText().trim();
        if (eventoId.isEmpty() || estudianteId.isEmpty()) {
            mostrarError("Escriba el evento y el código del estudiante.");
            return;
        }
        try {
            Reserva reserva = reservarEntrada.solicitarReserva(eventoId, estudianteId);
            campoCodigo.setText(reserva.getCodigo());
            mostrarExito("Reserva pendiente. Confírmela antes de las "
                    + reserva.getVenceEn().format(FORMATO_HORA) + ". Código: " + reserva.getCodigo());
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void alConfirmar() {
        String eventoId = campoEventoId.getText().trim();
        String codigo = campoCodigo.getText().trim();
        if (eventoId.isEmpty() || codigo.isEmpty()) {
            mostrarError("Escriba el evento y el código de la reserva.");
            return;
        }
        try {
            Reserva reserva = reservarEntrada.confirmarReserva(eventoId, codigo);
            mostrarExito("Reserva confirmada. Presente este código QR en la entrada: "
                    + reserva.getCodigo());
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
