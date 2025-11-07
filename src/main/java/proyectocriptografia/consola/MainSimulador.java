package proyectocriptografia.consola;

/**
 * Clase principal que actua como punto de entrada del simulador.
 * Inicia la ejecucion del menu de consola.
 */
public class MainSimulador {

    /**
     * Metodo principal que inicia la aplicacion del simulador.
     * @param args argumentos de linea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        new MenuConsola().mostrar();
    }
}
