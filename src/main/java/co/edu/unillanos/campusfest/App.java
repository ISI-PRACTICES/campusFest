package co.edu.unillanos.campusfest;

import java.io.IOException;
import java.time.LocalDateTime;

import co.edu.unillanos.campusfest.application.port.Reloj;
import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.application.usecase.ConsultarCupos;
import co.edu.unillanos.campusfest.application.usecase.EditarEvento;
import co.edu.unillanos.campusfest.application.usecase.RegistrarIngreso;
import co.edu.unillanos.campusfest.application.usecase.ReservarEntrada;
import co.edu.unillanos.campusfest.domain.Evento;
import co.edu.unillanos.campusfest.infrastructure.persistence.RepositorioEventosEnMemoria;
import co.edu.unillanos.campusfest.infrastructure.reloj.RelojDelSistema;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaCuposController;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaEventoController;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaIngresoController;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaReservaController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Ensamblaje de CampusFest (anillo de frameworks y drivers).
 * Es la única clase que crea las implementaciones de los puertos y las pasa
 * a los casos de uso, y la única que crea los controladores de las pantallas.
 */
public class App extends Application {

    @Override
    public void start(Stage ventana) {
        try {
            RepositorioEventos repositorio = new RepositorioEventosEnMemoria();
            Reloj reloj = new RelojDelSistema();
            cargarEventosDeDemostracion(repositorio);

            TabPane pestanas = new TabPane();
            pestanas.getTabs().add(pestana("CU1 · Consultar cupos", "pantalla-cupos.fxml",
                    new PantallaCuposController(new ConsultarCupos(repositorio, reloj))));
            pestanas.getTabs().add(pestana("CU2 · Reservar entrada", "pantalla-reserva.fxml",
                    new PantallaReservaController(new ReservarEntrada(repositorio, reloj))));
            pestanas.getTabs().add(pestana("CU3 · Editar evento", "pantalla-evento.fxml",
                    new PantallaEventoController(new EditarEvento(repositorio, reloj))));
            pestanas.getTabs().add(pestana("CU4 · Registrar ingreso", "pantalla-ingreso.fxml",
                    new PantallaIngresoController(new RegistrarIngreso(repositorio, reloj))));

            Scene escena = new Scene(pestanas, 760, 520);
            escena.getStylesheets().add(App.class.getResource("vistas/estilos.css").toExternalForm());
            ventana.setTitle("CampusFest");
            ventana.setScene(escena);
            ventana.show();
        } catch (IOException | IllegalStateException e) {
            mostrarErrorDeArranque(e);
        }
    }

    private Tab pestana(String titulo, String vista, Object controlador) throws IOException {
        FXMLLoader cargador = new FXMLLoader(App.class.getResource("vistas/" + vista));
        cargador.setController(controlador);
        Parent contenido = cargador.load();
        Tab pestana = new Tab(titulo, contenido);
        pestana.setClosable(false);
        return pestana;
    }

    /** Ningún caso de uso crea eventos: si no hay datos, se dejan dos para probar. */
    private void cargarEventosDeDemostracion(RepositorioEventos repositorio) {
        if (repositorio.buscar("concierto") == null) {
            repositorio.guardar(new Evento("concierto", "Concierto de bienvenida",
                    LocalDateTime.of(2026, 11, 28, 19, 0), "Auditorio Barcelona", 120, 5));
        }
        if (repositorio.buscar("feria") == null) {
            repositorio.guardar(new Evento("feria", "Feria de emprendimiento",
                    LocalDateTime.of(2026, 12, 3, 9, 0), "Coliseo", 3, 1));
        }
    }

    private void mostrarErrorDeArranque(Exception error) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("CampusFest");
        alerta.setHeaderText("La aplicación no pudo iniciar");
        alerta.setContentText(error.getMessage());
        alerta.showAndWait();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
