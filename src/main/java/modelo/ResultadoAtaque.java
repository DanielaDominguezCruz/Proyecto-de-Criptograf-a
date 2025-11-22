package modelo;

/**
 * Clase que representa el resultado de un ataque criptográfico.
 * Contiene información sobre si fue exitoso, la palabra hallada y un mensaje descriptivo.
 */
public class ResultadoAtaque {
    private boolean exito;    // Indica si el ataque fue exitoso
    private String palabra;   // Palabra encontrada que coincide con el objetivo
    private String mensaje;   // Mensaje adicional sobre el resultado

    /**
     * Constructor de ResultadoAtaque.
     * @param exito Indica si el ataque tuvo éxito
     * @param palabra Palabra hallada
     * @param mensaje Mensaje descriptivo del resultado
     */
    public ResultadoAtaque(boolean exito, String palabra, String mensaje) {
        this.exito = exito;
        this.palabra = palabra;
        this.mensaje = mensaje;
    }

    /**
     * Indica si el ataque fue exitoso.
     * @return true si tuvo éxito, false en caso contrario
     */
    public boolean isExito() { return exito; }

    /**
     * Obtiene la palabra hallada en el ataque.
     * @return palabra encontrada
     */
    public String getPalabra() { return palabra; }

    /**
     * Obtiene el mensaje descriptivo del resultado del ataque.
     * @return mensaje del resultado
     */
    public String getMensaje() { return mensaje; }

    /**
     * Devuelve una representación en texto del resultado del ataque.
     * @return cadena con información de éxito, palabra hallada y mensaje
     */
    @Override
    public String toString() {
        return "Éxito: " + exito + "\n" +
               "Palabra encontrada: " + palabra + "\n" +
               "Mensaje: " + mensaje;
    }
}
