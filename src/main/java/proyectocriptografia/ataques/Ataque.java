package proyectocriptografia.ataques;

/**
 * Contrato básico de un ataque de recuperación de contraseñas.
 */
public interface Ataque extends Runnable {
    void configurar(ParametrosAtaque parametros);
    void iniciar();
    void detener();
    boolean estaEjecutando();
    long getIntentosRealizados();
    String getResultadoClave(); // null si no hallada
}
