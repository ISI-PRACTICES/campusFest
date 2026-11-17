package co.edu.unillanos.campusfest;

/**
 * Punto de entrada que no hereda de Application. Permite ejecutar la aplicación
 * desde un jar o desde el botón Run de VS Code sin el error
 * "JavaFX runtime components are missing".
 */
public class Launcher {

    public static void main(String[] args) {
        App.main(args);
    }
}
