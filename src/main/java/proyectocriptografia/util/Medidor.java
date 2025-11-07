package proyectocriptografia.util;

/**
 * Clase de utilidad para medicion de rendimiento y calculo de tasas de ejecucion
 */
public final class Medidor {
    
    /** Constructor privado para evitar instanciacion  */
    private Medidor(){}

    /** 
    * Calcula el promedio de intentos realizados por segunfo a partir del total de intentos y la duracion
    * @param totalIntentos cantidad total de intentos realizados
    * @param duracionMS duracion total de la ejecucion en milisegundos
    * @return numero promedio de intentos por segundos
    */
    public static double intentosPorSegundo(long totalIntentos, long duracionMs){
        if (duracionMs <= 0) return 0d;
        return (totalIntentos * 1000.0) / duracionMs;
    }
}
