package proyectocriptografia.util;

import java.time.Duration;
import java.text.DecimalFormat;

/**
 * Clase de utilidad para formatear valores numericos y temporales.
 */
public final class Formato {

    /** Formato decimal con dos cifras decimales. */
    private static final DecimalFormat DF2 = new DecimalFormat("#0.00");

    /** Constructor privado para evitar instanciacion. */
    private Formato(){}

    /**
     * Convierte un valor en milisegundos a una representacion legible en formato horas, minutos, segundos y milisegundos.
     * @param ms tiempo en milisegundos
     * @return cadena formateada como hh mm ss ms
     */
    public static String msAHumano(long ms){
        Duration d = Duration.ofMillis(ms);
        long h = d.toHours();
        long m = d.minusHours(h).toMinutes();
        long s = d.minusHours(h).minusMinutes(m).toSeconds();
        long msRest = d.minusHours(h).minusMinutes(m).minusSeconds(s).toMillis();
        return String.format("%02dh %02dm %02ds %03dms", h, m, s, msRest);
    }

    /**
     * Formatea un valor double con dos decimales.
     * @param v valor numerico a formatear
     * @return representacion del numero con dos cifras decimales
     */
    public static String double2(double v){ return DF2.format(v); }
}
