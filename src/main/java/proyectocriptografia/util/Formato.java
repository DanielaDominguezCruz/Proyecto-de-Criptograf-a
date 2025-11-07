package proyectocriptografia.util;

import java.time.Duration;
import java.text.DecimalFormat;

/**
 * Formatos auxiliares.
 */
public final class Formato {
    private static final DecimalFormat DF2 = new DecimalFormat("#0.00");
    private Formato(){}

    public static String msAHumano(long ms){
        Duration d = Duration.ofMillis(ms);
        long h = d.toHours();
        long m = d.minusHours(h).toMinutes();
        long s = d.minusHours(h).minusMinutes(m).toSeconds();
        long msRest = d.minusHours(h).minusMinutes(m).minusSeconds(s).toMillis();
        return String.format("%02dh %02dm %02ds %03dms", h, m, s, msRest);
    }
    public static String double2(double v){ return DF2.format(v); }
}
