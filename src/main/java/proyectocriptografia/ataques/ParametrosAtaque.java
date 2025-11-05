package proyectocriptografia.ataques;

import java.util.List;

/**
 * DTO con parámetros de configuración para los ataques.
 */
public class ParametrosAtaque {
    // Comunes
    public String hashObjetivo;
    public long maxIntentosMuestralesCada; // ej. 50 => guarda 1 de cada 50
    public long limiteTiempoMs; // 0 = sin límite

    // Fuerza bruta
    public String alfabeto;
    public int minLongitud;
    public int maxLongitud;

    // Diccionario
    public List<String> diccionario;

    public String toResumen(){
        return "hash="+hashObjetivo+"; limiteMs="+limiteTiempoMs
                +"; muestralCada="+maxIntentosMuestralesCada
                +"; alfabeto="+alfabeto+"; min="+minLongitud+"; max="+maxLongitud
                +"; dic="+(diccionario==null?0:diccionario.size())+" items";
    }
}
