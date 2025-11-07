package proyectocriptografia.util;

/**
 * Utilidad de validaciones simples.
 */
public final class Validacion {
    private Validacion(){}

    public static void requerirNoVacio(String valor, String nombre){
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El parametro '" + nombre + "' es obligatorio.");
        }
    }

    public static void requerirPositivo(long valor, String nombre){
        if (valor <= 0) throw new IllegalArgumentException(nombre + " debe ser > 0");
    }
}
