package modelo;

import proyectocriptografia.util.ComparadorHash;
import java.util.concurrent.atomic.AtomicBoolean;
import proyectocriptografia.ataques.Ataque;
import proyectocriptografia.ataques.GeneradorCombinaciones;
import proyectocriptografia.ataques.ParametrosAtaque;

/**
 * Implementación de un ataque de fuerza bruta.
 * Genera todas las combinaciones posibles según un alfabeto, longitud mínima y máxima,
 * comparando cada intento con el hash objetivo.
 */
public class AtaqueFuerzaBruta implements Ataque {

    private ParametrosAtaque p; // Parámetros del ataque (alfabeto, longitudes, hash objetivo, etc.)
    private final AtomicBoolean ejecutando = new AtomicBoolean(false); // Indica si el ataque está en ejecución
    private volatile long intentos; // Cantidad de intentos realizados
    private volatile String hallada; // Clave encontrada, si coincide el hash

    /**
     * Configura los parámetros del ataque de fuerza bruta.
     * @param parametros Parámetros de ataque necesarios para generar combinaciones
     */
    @Override
    public void configurar(ParametrosAtaque parametros) {
        this.p = parametros;
    }

    /**
     * Indica si el ataque se encuentra ejecutándose.
     * @return true si está ejecutándose, false en caso contrario
     */
    @Override
    public boolean estaEjecutando() {
        return ejecutando.get();
    }

    /**
     * Retorna el número de intentos realizados.
     * @return cantidad de intentos
     */
    @Override
    public long getIntentosRealizados() {
        return intentos;
    }

    /**
     * Devuelve la clave encontrada que genera el hash objetivo.
     * @return clave hallada o null si no se encontró
     */
    @Override
    public String getResultadoClave() {
        return hallada;
    }

    /**
     * Ejecuta el ataque generando combinaciones y comparándolas con el hash objetivo.
     * Respeta el límite de tiempo y permite detener la ejecución.
     */
    @Override
    public void run() {
        ejecutando.set(true);
        long inicio = System.currentTimeMillis();

        try {
            var gen = new GeneradorCombinaciones(p.alfabeto, p.minLongitud, p.maxLongitud);
            long i = 0;

            for (String cand : gen) {
                if (!ejecutando.get()) break; // Se solicitó detener el ataque

                i++;

                // Comparar hash del candidato con el hash objetivo
                if (ComparadorHash.coincideSha256(cand, p.hashObjetivo)) {
                    hallada = cand;
                    break;
                }

                // Verificar límite de tiempo
                if (p.limiteTiempoMs > 0 &&
                   (System.currentTimeMillis() - inicio >= p.limiteTiempoMs)) {
                    break;
                }
            }
            intentos = i;

        } finally {
            ejecutando.set(false); // Indica que el ataque ha finalizado
        }
    }

    /**
     * Inicia el ataque en un nuevo hilo.
     */
    @Override
    public void iniciar() {
        new Thread(this, "ataque-fuerza-bruta").start();
    }

    /**
     * Detiene la ejecución del ataque.
     */
    @Override
    public void detener() {
        ejecutando.set(false);
    }
}
