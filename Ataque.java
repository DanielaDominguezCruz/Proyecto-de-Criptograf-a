package proyectocriptografia.ataques;

/**
 * Contrato básico para un ataque de recuperación de contraseñas.
 * <p>
 * Esta interfaz extiende {@link Runnable} para permitir la ejecución del ataque
 * en un hilo separado si se desea. Implementaciones concretas deben definir
 * la lógica de configuración, inicio, parada y reporte de resultados.
 * </p>
 *
 * <p>
 * Comportamiento esperado:
 * <ul>
 *   <li>{@link #configurar(ParametrosAtaque)} prepara el ataque con los parámetros necesarios.</li>
 *   <li>{@link #iniciar()} comienza la ejecución del ataque (puede lanzar o delegar a {@code run()}).</li>
 *   <li>{@link #detener()} solicita la terminación del ataque de forma ordenada.</li>
 *   <li>{@link #estaEjecutando()} informa si el ataque está en curso.</li>
 *   <li>{@link #getIntentosRealizados()} devuelve el número de intentos (uso para métricas/estadísticas).</li>
 *   <li>{@link #getResultadoClave()} devuelve la clave encontrada o {@code null} si no se halló ninguna.</li>
 * </ul>
 * </p>
 *
 * @see Runnable
 * @since 1.0
 */
public interface Ataque extends Runnable {

    /**
     * Configura el ataque con los parámetros necesarios antes de su ejecución.
     *
     * <p>Este método debe ser llamado antes de {@link #iniciar()} (o antes de ejecutar
     * el {@link Runnable} si se usa {@code run()} directamente). Las implementaciones
     * deberían validar y almacenar los valores entregados en {@code parametros}.</p>
     *
     * @param parametros instancia que contiene los parámetros del ataque (por ejemplo:
     *                   objetivo, diccionario, rango de claves, límites de tiempo, etc.)
     * @throws IllegalArgumentException si {@code parametros} es {@code null} o contiene valores inválidos
     */
    void configurar(ParametrosAtaque parametros);

    /**
     * Inicia la ejecución del ataque.
     *
     * <p>Dependiendo de la implementación, este método puede:
     * <ul>
     *   <li>lanzar la ejecución en el mismo hilo (sincronamente),</li>
     *   <li>o crear/arrancar un hilo nuevo y delegar en {@code run()}.</li>
     * </ul>
     * En cualquier caso, después de llamar a este método, {@link #estaEjecutando()} debería
     * reflejar el estado correcto.</p>
     *
     * @throws IllegalStateException si el ataque no fue configurado correctamente o ya está en ejecución
     */
    void iniciar();

    /**
     * Solicita la detención ordenada del ataque en ejecución.
     *
     * <p>Este método debe intentar detener el proceso de ataque de forma segura
     * y permitir que los recursos se liberen. No está garantizado que la parada
     * sea inmediata; por ello {@link #estaEjecutando()} puede seguir devolviendo
     * {@code true} hasta que la detención se complete.</p>
     */
    void detener();

    /**
     * Indica si el ataque se encuentra en ejecución.
     *
     * @return {@code true} si el ataque está actualmente en curso, {@code false} en caso contrario
     */
    boolean estaEjecutando();

    /**
     * Devuelve el número total de intentos realizados hasta el momento.
     *
     * <p>Este contador puede usarse para monitorizar el progreso del ataque o para
     * calcular tasas de intentos por segundo.</p>
     *
     * @return número de intentos realizados (>= 0)
     */
    long getIntentosRealizados();

    /**
     * Obtiene la clave (contraseña) encontrada por el ataque, si corresponde.
     *
     * @return la clave encontrada como {@link String}, o {@code null} si aún no se ha hallado
     *         ninguna clave o si el ataque finalizó sin éxito.
     */
    String getResultadoClave(); // null si no hallada
}

