package proyectocriptografia.util;

/**
 * Clase de utilidad para realizar validaciones simples de parametros.
 */
public final class Validacion {

    /** Constructor privado para evitar instanciacion. */
    private Validacion(){}

    /**
     * Verifica que un valor de texto no sea nulo ni vacio.
     * @param valor texto a validar
     * @param nombre nombre descriptivo del parametro para mensajes de error
     * @throws IllegalArgumentException si el valor es nulo o vacio
     */
    public static void requerirNoVacio(String valor, String nombre){
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El parametro '" + nombre + "' es obligatorio.");
        }
    }

    /**
     * Verifica que un valor numerico sea positivo.
     * @param valor valor numerico a validar
     * @param nombre nombre descriptivo del parametro para mensajes de error
     * @throws IllegalArgumentException si el valor es menor o igual a cero
     */
    public static void requerirPositivo(long valor, String nombre){
        if (valor <= 0) throw new IllegalArgumentException(nombre + " debe ser > 0");
    }
}
