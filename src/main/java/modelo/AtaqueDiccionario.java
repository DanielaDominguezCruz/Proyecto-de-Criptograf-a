package modelo;

import proyectocriptografia.util.ComparadorHash;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import proyectocriptografia.ataques.Ataque;
import proyectocriptografia.ataques.ParametrosAtaque;

public class AtaqueDiccionario implements Ataque {

    private ParametrosAtaque p;
    private final AtomicBoolean ejecutando = new AtomicBoolean(false);
    private volatile long intentos;
    private volatile String hallada;

    // ✔ Constructor vacío (el ataque se configura después)
    public AtaqueDiccionario() {
    }

    @Override
    public void configurar(ParametrosAtaque parametros) {
        this.p = parametros;
    }

    @Override
    public boolean estaEjecutando() {
        return ejecutando.get();
    }

    @Override
    public long getIntentosRealizados() {
        return intentos;
    }

    @Override
    public String getResultadoClave() {
        return hallada;
    }

    @Override
    public void run() {
        ejecutando.set(true);
        long inicio = System.currentTimeMillis();

        try {
            List<String> dic = p.diccionario;
            long i = 0;
            for (String palabra : dic) {
                if (!ejecutando.get())
                    break;

                i++;

                if (ComparadorHash.coincideSha256(palabra, p.hashObjetivo)) {
                    hallada = palabra;
                    break;
                }

                if (p.limiteTiempoMs > 0 &&
                    (System.currentTimeMillis() - inicio) >= p.limiteTiempoMs)
                    break;
            }
            intentos = i;

        } finally {
            ejecutando.set(false);
        }
    }

    @Override
    public void iniciar() {
        new Thread(this, "ataque-diccionario").start();
    }

    @Override
    public void detener() {
        ejecutando.set(false);
    }
}


