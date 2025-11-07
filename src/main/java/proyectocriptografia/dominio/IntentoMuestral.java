package proyectocriptografia.dominio;

import java.time.Instant;

/**
 * Representa un intento muestral registrado durante una simulacion.
 * Se utiliza para almacenar informacion parcial de los intentos con fines de auditoria o analisis.
 */
public class IntentoMuestral {

    /** Identificador unico del intento muestral. */
    private Long id;

    /** Identificador de la simulacion a la que pertenece el intento. */
    private Long idSimulacion;

    /** Numero de intento dentro de la simulacion. */
    private long indice;

    /** Valor propuesto en el intento. */
    private String valorPropuesto;

    /** Marca de tiempo en la que se registro el intento. */
    private Instant timestamp;

    /**
     * Obtiene el identificador unico del intento.
     * @return identificador del intento
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador unico del intento.
     * @param id identificador del intento
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el identificador de la simulacion asociada.
     * @return identificador de la simulacion
     */
    public Long getIdSimulacion() { return idSimulacion; }

    /**
     * Establece el identificador de la simulacion asociada.
     * @param idSimulacion identificador de la simulacion
     */
    public void setIdSimulacion(Long idSimulacion) { this.idSimulacion = idSimulacion; }

    /**
     * Obtiene el numero de intento dentro de la simulacion.
     * @return numero de intento
     */
    public long getIndice() { return indice; }

    /**
     * Establece el numero de intento dentro de la simulacion.
     * @param indice numero de intento
     */
    public void setIndice(long indice) { this.indice = indice; }

    /**
     * Obtiene el valor propuesto en el intento.
     * @return valor propuesto
     */
    public String getValorPropuesto() { return valorPropuesto; }

    /**
     * Establece el valor propuesto en el intento.
     * @param valorPropuesto valor propuesto
     */
    public void setValorPropuesto(String valorPropuesto) { this.valorPropuesto = valorPropuesto; }

    /**
     * Obtiene la marca de tiempo del intento.
     * @return marca de tiempo del intento
     */
    public Instant getTimestamp() { return timestamp; }

    /**
     * Establece la marca de tiempo del intento.
     * @param timestamp marca de tiempo del intento
     */
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
