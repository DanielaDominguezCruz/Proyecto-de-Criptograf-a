package proyectocriptografia.ataques;

import java.util.List;

/**
 * Clase que almacena los parámetros de configuración
 * necesarios para ejecutar diferentes tipos de ataques
 * de criptografía (como fuerza bruta o diccionario).
 */
public class ParametrosAtaque {

    /** Hash que se desea descifrar o encontrar. */
    public String hashObjetivo;

    /** 
     * Número máximo de intentos antes de guardar una muestra.  
     * Ejemplo: si el valor es 50, se guardará 1 de cada 50 intentos.
     */
    public long maxIntentosMuestralesCada;

    /** 
     * Tiempo máximo permitido para ejecutar el ataque (en milisegundos).  
     * Si el valor es 0, significa que no hay límite de tiempo.
     */
    public long limiteTiempoMs;

    // ==================== Parámetros para ataque de fuerza bruta ====================

    /** Conjunto de caracteres que se usarán para generar combinaciones. */
    public String alfabeto;

    /** Longitud mínima de las combinaciones generadas. */
    public int minLongitud;

    /** Longitud máxima de las combinaciones generadas. */
    public int maxLongitud;

    // ==================== Parámetros para ataque de diccionario ====================

    /** Lista de palabras que se usarán como posibles contraseñas. */
    public List<String> diccionario;

    /**
     * Devuelve un resumen con los valores principales de la configuración.
     *
     * @return Cadena con los parámetros resumidos.
     */
    public String toResumen() {
        return "hash=" + hashObjetivo +
                "; limiteMs=" + limiteTiempoMs +
                "; muestralCada=" + maxIntentosMuestralesCada +
                "; alfabeto=" + alfabeto +
                "; min=" + minLongitud +
                "; max=" + maxLongitud +
                "; dic=" + (diccionario == null ? 0 : diccionario.size()) + " items";
    }
}
