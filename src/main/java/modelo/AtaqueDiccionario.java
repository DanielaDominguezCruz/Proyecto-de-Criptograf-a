package modelo;

import proyectocriptografia.util.ComparadorHash;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import proyectocriptografia.ataques.Ataque;
import proyectocriptografia.ataques.ParametrosAtaque;

/**
 * Implementación de un ataque de diccionario.
 * Permite buscar la clave que genera un hash objetivo recorriendo un diccionario de palabras.
 */
public class AtaqueDiccionario implements Ataque {

    private ParametrosAtaque p; // Parámetros del ataque (diccionario, hash objetivo, límite de tiempo)
    private final AtomicBoolean ejecutando = new AtomicBoolean(false); // Indica si el ataque está en ejecución
    private volatile long intentos; // Contador de intentos realizados
    private volatile String hallada; // Clave encontrada, si coincide con el hash

    /**
     * Constructor vacío; la configuración se realiza posteriormente mediante configurar().
     */
    public AtaqueDiccionario() {
    }

    /**
     * Configura los parámetros del ataque.
     * @param parametros Parámetros del ataque de diccionario
     */
    @Override
    public void configurar(ParametrosAtaque parametros) {
        this.p = parametros;
    }

    /**
     * Indica si el ataque está actualmente en ejecución.
     * @return true si se está ejecutando, false en caso contrario
     */
    @Override
    public boolean estaEjecutando() {
        return ejecutando.get();
    }

    /**
     * Retorna el número de intentos realizados hasta el momento.
     * @return cantidad de intentos
     */
    @Override
    public long getIntentosRealizados() {
        return intentos;
    }

    /**
     * Retorna la clave encontrada que coincide con el hash objetivo.
     * @return clave hallada o null si no se encontró
     */
    @Override
    public String getResultadoClave() {
        return hallada;
    }

    /**
     * Ejecuta el ataque recorriendo el diccionario y comparando cada palabra con el hash objetivo.
     * Respeta el límite de tiempo y permite detener la ejecución.
     */
    @Override
    public void run() {
        ejecutando.set(true);
        long inicio = System.currentTimeMillis();

        try {
            List<String> dic = p.diccionario;
            long i = 0;
            for (String palabra : dic) {
                if (!ejecutando.get())
                    break; // Detener si se solicita

                i++;

                // Comparar el hash de la palabra con el hash objetivo
                if (ComparadorHash.coincideSha256(palabra, p.hashObjetivo)) {
                    hallada = palabra;
                    break;
                }

                // Verificar límite de tiempo
                if (p.limiteTiempoMs > 0 &&
                    (System.currentTimeMillis() - inicio) >= p.limiteTiempoMs)
                    break;
            }
            intentos = i;

        } finally {
            ejecutando.set(false); // Marcar ataque como finalizado
        }
    }

    /**
     * Inicia el ataque en un nuevo hilo.
     */
    @Override
    public void iniciar() {
        new Thread(this, "ataque-diccionario").start();
    }

    /**
     * Detiene la ejecución del ataque.
     */
    @Override
    public void detener() {
        ejecutando.set(false);
    }
}
