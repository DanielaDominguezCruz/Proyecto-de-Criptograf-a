package proyectocriptografia.util;

/**
 * Cronometría y métricas de rendimiento.
 */
public final class Medidor {
    private Medidor(){}

    public static double intentosPorSegundo(long totalIntentos, long duracionMs){
        if (duracionMs <= 0) return 0d;
        return (totalIntentos * 1000.0) / duracionMs;
    }
}
