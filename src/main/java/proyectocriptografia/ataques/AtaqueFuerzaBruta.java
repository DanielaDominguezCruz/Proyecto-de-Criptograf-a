package proyectocriptografia.ataques;

import proyectocriptografia.util.ComparadorHash;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ataque de fuerza bruta que recorre todas las combinaciones posibles construidas
 * a partir de un alfabeto dado entre una longitud mínima y máxima.
 *
 * <p>La ejecución se realiza en un hilo distinto cuando se llama a {@link #iniciar()}.
 * La búsqueda puede detenerse voluntariamente con {@link #detener()} o
 * por agotamiento del tiempo límite especificado en los parámetros.</p>
 *
 * <p>Estado y concurrencia:</p>
 * <ul>
 *   <li>{@code ejecutando} (AtomicBoolean) indica si el ataque está en ejecución
 *       y permite una parada segura desde otro hilo.</li>
 *   <li>{@code intentos} y {@code hallada} son variables {@code volatile} para
 *       garantizar visibilidad entre hilos: {@code intentos} almacena el número
 *       de candidatos probados y {@code hallada} contiene la clave encontrada
 *       (si la hay).</li>
 * </ul>
 *
 * @see proyectocriptografia.ataques.GeneradorCombinaciones
 * @see proyectocriptografia.util.ComparadorHash
 */
public class AtaqueFuerzaBruta implements Ataque {
    /**
     * Parámetros que controlan el ataque (alfabeto, longitudes, hash objetivo,
     * límite de tiempo, etc.). Se asignan mediante {@link #configurar(ParametrosAtaque)}.
     */
    private ParametrosAtaque p;

    /**
     * Indicador atómico de ejecución: {@code true} mientras el hilo de ataque está activo.
     * Usado para solicitar una detención segura desde otro hilo.
     */
    private final AtomicBoolean ejecutando = new AtomicBoolean(false);

    /**
     * Contador de intentos realizados. Se escribe desde el hilo del ataque y se
     * lee desde hilos externos, por eso es {@code volatile} para visibilidad.
     */
    private volatile long intentos;

    /**
     * Candidate clave encontrada que hace coincidir el hash objetivo. {@code volatile}
     * para que otros hilos vean el valor actualizado inmediatamente.
     */
    private volatile String hallada;

    /**
     * Configura los parámetros del ataque.
     *
     * @param parametros valores que controlan el ataque:
     *                    <ul>
     *                      <li>{@code alfabeto}: caracteres a usar para generar candidatos</li>
     *                      <li>{@code minLongitud} y {@code maxLongitud}: rango de longitudes a probar</li>
     *                      <li>{@code hashObjetivo}: hash que se desea encontrar</li>
     *                      <li>{@code limiteTiempoMs}: tiempo máximo en milisegundos (0 = sin límite)</li>
     *                    </ul>
     */
    @Override public void configurar(ParametrosAtaque parametros) { this.p = parametros; }

    /**
     * Indica si el ataque está actualmente en ejecución.
     *
     * @return {@code true} si el hilo del ataque está activo; {@code false} en caso contrario.
     */
    @Override public boolean estaEjecutando(){ return ejecutando.get(); }

    /**
     * Devuelve el número de intentos (candidatos probados) realizados hasta el momento.
     *
     * @return número de intentos realizados (valor eventual, puede cambiar si el ataque sigue en ejecución).
     */
    @Override public long getIntentosRealizados(){ return intentos; }

    /**
     * Devuelve la clave encontrada que coincide con el hash objetivo, o {@code null}
     * si no se ha encontrado ninguna.
     *
     * @return la clave hallada o {@code null}.
     */
    @Override public String getResultadoClave(){ return hallada; }

    /**
     * Núcleo del ataque: genera candidatos usando {@link GeneradorCombinaciones} y
     * compara su hash (SHA-256) con el hash objetivo mediante
     * {@link ComparadorHash#coincideSha256(String, String)}.
     *
     * <p>Condiciones de parada (se detiene cuando ocurra cualquiera de estas):
     * <ol>
     *   <li>Se llama a {@link #detener()} (la bandera atómica {@code ejecutando} se pone a {@code false}).</li>
     *   <li>Se encuentra un candidato cuyo hash coincide con el hash objetivo; en ese caso
     *       se almacena la clave en {@link #hallada}.</li>
     *   <li>Se alcanza el límite de tiempo especificado en {@link ParametrosAtaque#limiteTiempoMs}
     *       (si {@code limiteTiempoMs > 0}).</li>
     *   <li>Se agotan las combinaciones generadas por {@link GeneradorCombinaciones}.</li>
     * </ol></p>
     *
     * <p>Al finalizar (por cualquier motivo) la bandera {@code ejecutando} se establece a {@code false}
     * en el bloque {@code finally} para asegurar un estado consistente.</p>
     *
     * <p>Notas de concurrencia: {@code intentos} se asigna al finalizar el bucle para reflejar
     * el número total de candidatos procesados; mientras se ejecuta, el valor leído desde
     * otro hilo puede ser una estimación parcial.</p>
     */
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

    /**
     * Inicia la ejecución del ataque en un nuevo hilo. El hilo creado tiene nombre
     * {@code "ataque-fuerza-bruta"} para facilitar su identificación en depuración.
     *
     * <p>Este método retorna inmediatamente tras lanzar el hilo.</p>
     */
    @Override public void iniciar() { new Thread(this, "ataque-fuerza-bruta").start(); }

    /**
     * Solicita la detención del ataque poniendo la bandera {@code ejecutando} a {@code false}.
     * La detención es cooperativa: el hilo de ataque comprobará la bandera y saldrá del bucle
     * lo antes posible. Tras la finalización efectiva, {@link #estaEjecutando()} devolverá {@code false}.
     */
    @Override public void detener() { ejecutando.set(false); }
}
