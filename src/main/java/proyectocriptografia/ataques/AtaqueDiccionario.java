package proyectocriptografia.ataques;

import proyectocriptografia.util.ComparadorHash;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ataque de diccionario: recorre palabras de una lista.
 */
public class AtaqueDiccionario implements Ataque {
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
            List<String> dic = p.diccionario;
            long i=0;
            for (String palabra : dic) {
                if (!ejecutando.get()) break;
                i++;
                if (ComparadorHash.coincideSha256(palabra, p.hashObjetivo)) {
                    hallada = palabra; break;
                }
                if (p.limiteTiempoMs>0 && (System.currentTimeMillis()-inicio)>=p.limiteTiempoMs) break;
            }
            intentos = i;
        } finally { ejecutando.set(false); }
    }

    @Override public void iniciar() { new Thread(this, "ataque-diccionario").start(); }
    @Override public void detener() { ejecutando.set(false); }
}
