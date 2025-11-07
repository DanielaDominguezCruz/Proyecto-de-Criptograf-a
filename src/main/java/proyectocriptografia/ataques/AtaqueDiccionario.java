package proyectocriptografia.ataques;

import proyectocriptografia.util.ComparadorHash;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ataque de diccionario: recorre una lista de palabras (diccionario) y compara
 * el hash SHA-256 de cada palabra con el hash objetivo proporcionado en
 * {@link ParametrosAtaque}.
 *
 * <p>
 * Esta implementación es segura para ejecutar desde varios hilos en lo que
 * respecta a la observación del estado: usa {@link AtomicBoolean} para el
 * control de ejecución y campos {@code volatile} para que las actualizaciones
 * de {@code intentos} y {@code hallada} sean visibles inmediatamente desde
 * otros hilos.
 * </p>
 *
 * <p>
 * Comportamiento relevante:
 * <ul>
 *   <li>Antes de iniciar el ataque se debe llamar a {@link #configurar(ParametrosAtaque)}
 *       para proporcionar los parámetros necesarios.</li>
 *   <li>El método {@link #iniciar()} crea y arranca un nuevo hilo nombrado
 *       {@code "ataque-diccionario"} que ejecuta {@link #run()}.</li>
 *   <li>Se puede solicitar la parada mediante {@link #detener()}; la detención
 *       es cooperativa (el bucle comprueba periódicamente el indicador
 *       {@code ejecutando}).</li>
 *   <li>Si se encuentra la palabra cuya huella coincide con el hash objetivo,
 *       ésta se guarda en {@link #getResultadoClave()} y la ejecución finaliza.</li>
 *   <li>Si {@link ParametrosAtaque#limiteTiempoMs} es mayor que 0, el ataque
 *       también finalizará cuando se exceda ese límite de tiempo.</li>
 * </ul>
 * </p>
 *
 * @see Ataque
 * @see ParametrosAtaque
 * @since 1.0
 */
public class AtaqueDiccionario implements Ataque {

    /**
     * Parámetros del ataque; debe establecerse mediante {@link #configurar(ParametrosAtaque)}
     * antes de iniciar la ejecución.
     */
    private ParametrosAtaque p;

    /**
     * Indicador de ejecución. Si es {@code true} el ataque continúa; si se
     * pone a {@code false} las rutinas cooperativas interrumpen la ejecución.
     *
     * Se usa {@link AtomicBoolean} para operaciones atómicas y lectura/escritura
     * seguras entre hilos.
     */
    private final AtomicBoolean ejecutando = new AtomicBoolean(false);

    /**
     * Número de intentos (palabras comprobadas) realizados hasta ahora.
     * Se marca {@code volatile} para visibilidad entre hilos sin sincronización
     * explícita.
     */
    private volatile long intentos;

    /**
     * Resultado (palabra) encontrada que produce el hash objetivo, o {@code null}
     * si no se ha encontrado ninguna o si el ataque aún no finalizó con éxito.
     * Marcado {@code volatile} para visibilidad entre hilos.
     */
    private volatile String hallada;

    /**
     * Configura el ataque con los parámetros proporcionados.
     *
     * <p>Debe llamarse antes de {@link #iniciar()} para establecer el diccionario,
     * el hash objetivo y el posible límite de tiempo.</p>
     *
     * @param parametros instancia de {@link ParametrosAtaque} que contiene:
     *                   <ul>
     *                     <li>{@code diccionario}: {@link List}{@code <String>} con las palabras a probar</li>
     *                     <li>{@code hashObjetivo}: {@code String} con el hash SHA-256 a igualar</li>
     *                     <li>{@code limiteTiempoMs}: límite en milisegundos (<=0 significa sin límite)</li>
     *                   </ul>
     * @see ParametrosAtaque
     */
    @Override
    public void configurar(ParametrosAtaque parametros) { this.p = parametros; }

    /**
     * Indica si el ataque se encuentra en ejecución.
     *
     * @return {@code true} si el ataque está en curso; {@code false} en caso contrario
     * @see #iniciar()
     * @see #detener()
     */
    @Override
    public boolean estaEjecutando(){ return ejecutando.get(); }

    /**
     * Devuelve el número de intentos (palabras comprobadas) realizados.
     *
     * @return número de intentos realizados (>= 0)
     */
    @Override
    public long getIntentosRealizados(){ return intentos; }

    /**
     * Devuelve la palabra encontrada que coincide con el hash objetivo, o {@code null}
     * si no se ha encontrado ninguna.
     *
     * @return la clave (palabra) encontrada, o {@code null} si no existe
     */
    @Override
    public String getResultadoClave(){ return hallada; }

    /**
     * Lógica principal del ataque: recorre el diccionario y compara el hash SHA-256
     * de cada palabra con el hash objetivo usando {@link ComparadorHash#coincideSha256(String, String)}.
     *
     * <p>
     * El bucle termina en cualquiera de las siguientes condiciones:
     * <ul>
     *   <li>se solicita la detención mediante {@link #detener()} (cooperativo);</li>
     *   <li>se encuentra la palabra que produce el hash objetivo (se guarda en {@link #hallada});</li>
     *   <li>se supera el límite de tiempo especificado en {@link ParametrosAtaque#limiteTiempoMs}
     *       (si > 0);</li>
     *   <li>se agotan las palabras del diccionario.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Al terminar (normalmente o por excepción), el indicador {@code ejecutando}
     * se pone a {@code false} en el bloque {@code finally} para garantizar que el
     * estado quede consistente.
     * </p>
     *
     * @see ComparadorHash#coincideSha256(String, String)
     */
    @Override
    public void run() {
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

    /**
     * Inicia el ataque en un nuevo hilo nombrado {@code "ataque-diccionario"}.
     *
     * <p>Este método no bloquea; lanza un hilo que ejecutará {@link #run()}.</p>
     *
     * @throws IllegalStateException si no se han proporcionado parámetros mediante {@link #configurar(ParametrosAtaque)}
     *                               (opcional: las implementaciones clientes pueden querer validar esto)
     */
    @Override
    public void iniciar() { new Thread(this, "ataque-diccionario").start(); }

    /**
     * Solicita la detención cooperativa del ataque estableciendo el indicador
     * de ejecución a {@code false}. La detención puede no ser inmediata: el
     * hilo en ejecución comprobará el indicador y saldrá cuando esté en un
     * punto de comprobación.
     */
    @Override
    public void detener() { ejecutando.set(false); }
}
