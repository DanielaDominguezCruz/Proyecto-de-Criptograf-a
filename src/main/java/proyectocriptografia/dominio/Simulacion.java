package proyectocriptografia.dominio;

import java.time.Instant;

/**
 * Representa una simulacion de ataque criptografico ejecutada por el sistema.
 * Contiene informacion sobre el tipo de ataque, parametros, resultados y rendimiento.
 */
public class Simulacion {

    /** Identificador unico de la simulacion. */
    private Long id;

    /** Tipo de simulacion ejecutada (por ejemplo, FUERZA_BRUTA o DICCIONARIO). */
    private String tipo;

    /** Hash objetivo que se intento descifrar durante la simulacion. */
    private String objetivoHash;

    /** Resumen de los parametros utilizados en la simulacion. */
    private String parametros;

    /** Marca de tiempo de inicio de la simulacion. */
    private Instant inicio;

    /** Marca de tiempo de finalizacion de la simulacion. */
    private Instant fin;

    /** Indica si la simulacion encontro la clave correcta. */
    private boolean exito;

    /** Clave hallada como resultado del ataque, si existio. */
    private String claveHallada;

    /** Numero total de intentos realizados durante la simulacion. */
    private long intentosTotales;

    /** Promedio de intentos realizados por segundo. */
    private double intentosPorSegundo;

    /**
     * Obtiene el identificador unico de la simulacion.
     * @return identificador de la simulacion
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador unico de la simulacion.
     * @param id identificador de la simulacion
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el tipo de simulacion.
     * @return tipo de simulacion
     */
    public String getTipo() { return tipo; }

    /**
     * Establece el tipo de simulacion.
     * @param tipo tipo de simulacion
     */
    public void setTipo(String tipo) { this.tipo = tipo; }

    /**
     * Obtiene el hash objetivo que se intento descifrar.
     * @return hash objetivo
     */
    public String getObjetivoHash() { return objetivoHash; }

    /**
     * Establece el hash objetivo que se intento descifrar.
     * @param objetivoHash hash objetivo
     */
    public void setObjetivoHash(String objetivoHash) { this.objetivoHash = objetivoHash; }

    /**
     * Obtiene el resumen de los parametros usados en la simulacion.
     * @return resumen de parametros
     */
    public String getParametros() { return parametros; }

    /**
     * Establece el resumen de los parametros usados en la simulacion.
     * @param parametros resumen de parametros
     */
    public void setParametros(String parametros) { this.parametros = parametros; }

    /**
     * Obtiene la marca de tiempo de inicio de la simulacion.
     * @return instante de inicio
     */
    public Instant getInicio() { return inicio; }

    /**
     * Establece la marca de tiempo de inicio de la simulacion.
     * @param inicio instante de inicio
     */
    public void setInicio(Instant inicio) { this.inicio = inicio; }

    /**
     * Obtiene la marca de tiempo de finalizacion de la simulacion.
     * @return instante de finalizacion
     */
    public Instant getFin() { return fin; }

    /**
     * Establece la marca de tiempo de finalizacion de la simulacion.
     * @param fin instante de finalizacion
     */
    public void setFin(Instant fin) { this.fin = fin; }

    /**
     * Indica si la simulacion tuvo exito.
     * @return true si se encontro la clave, false en caso contrario
     */
    public boolean isExito() { return exito; }

    /**
     * Define si la simulacion tuvo exito.
     * @param exito true si se encontro la clave, false en caso contrario
     */
    public void setExito(boolean exito) { this.exito = exito; }

    /**
     * Obtiene la clave hallada durante la simulacion.
     * @return clave hallada o null si no se encontro
     */
    public String getClaveHallada() { return claveHallada; }

    /**
     * Establece la clave hallada durante la simulacion.
     * @param claveHallada clave hallada
     */
    public void setClaveHallada(String claveHallada) { this.claveHallada = claveHallada; }

    /**
     * Obtiene el numero total de intentos realizados.
     * @return total de intentos
     */
    public long getIntentosTotales() { return intentosTotales; }

    /**
     * Establece el numero total de intentos realizados.
     * @param intentosTotales total de intentos
     */
    public void setIntentosTotales(long intentosTotales) { this.intentosTotales = intentosTotales; }

    /**
     * Obtiene el promedio de intentos realizados por segundo.
     * @return intentos por segundo
     */
    public double getIntentosPorSegundo() { return intentosPorSegundo; }

    /**
     * Establece el promedio de intentos realizados por segundo.
     * @param intentosPorSegundo intentos por segundo
     */
    public void setIntentosPorSegundo(double intentosPorSegundo) { this.intentosPorSegundo = intentosPorSegundo; }

    /**
     * Devuelve una representacion en texto del objeto Simulacion.
     * @return cadena con informacion resumida de la simulacion
     */
    @Override
    public String toString() {
        return "Simulacion{id=%d,tipo=%s,exito=%b,intentos=%d,ips=%.2f}".formatted(
                id, tipo, exito, intentosTotales, intentosPorSegundo);
    }
}
