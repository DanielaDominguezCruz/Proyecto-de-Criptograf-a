package proyectocriptografia.ataques;

import proyectocriptografia.util.ComparadorHash;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ataque de fuerza bruta: recorre combinaciones de un alfabeto entre longitudes.
 */
public class AtaqueFuerzaBruta implements Ataque {
    private ParametrosAtaque p;
    private final AtomicBoolean ejecutando = new AtomicBoolean(false);
    private volatile long intentos;
    private volatile String hallada;

    @Override public void configurar(ParametrosAtaque parametros) { this.p = parametros; }
    @Override public boolean estaEjecutando(){ return ejecutando.get(); }
    @Override public long getIntentosRealizados(){ return intentos; }
    @Override public String getResultadoClave(){ return hallada; }

    @Override public void run() {
        ejecutando.set(true);
        long inicio = System.currentTimeMillis();
        try {
            var gen = new GeneradorCombinaciones(p.alfabeto, p.minLongitud, p.maxLongitud);
            long i = 0;
            for (String cand : gen) {
                if (!ejecutando.get()) break;
                i++;
                if (ComparadorHash.coincideSha256(cand, p.hashObjetivo)) {
                    hallada = cand; break;
                }
                if (p.limiteTiempoMs>0 && (System.currentTimeMillis()-inicio)>=p.limiteTiempoMs) break;
            }
            intentos = i;
        } finally {
            ejecutando.set(false);
        }
    }

    @Override public void iniciar() { new Thread(this, "ataque-fuerza-bruta").start(); }
    @Override public void detener() { ejecutando.set(false); }
}
