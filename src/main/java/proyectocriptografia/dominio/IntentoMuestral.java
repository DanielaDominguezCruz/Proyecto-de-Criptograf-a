package proyectocriptografia.dominio;

import java.time.Instant;

/**
 * Intento almacenado de forma muestral para auditoría.
 */
public class IntentoMuestral {
    private Long id;
    private Long idSimulacion;
    private long indice;
    private String valorPropuesto;
    private Instant timestamp;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdSimulacion() { return idSimulacion; }
    public void setIdSimulacion(Long idSimulacion) { this.idSimulacion = idSimulacion; }
    public long getIndice() { return indice; }
    public void setIndice(long indice) { this.indice = indice; }
    public String getValorPropuesto() { return valorPropuesto; }
    public void setValorPropuesto(String valorPropuesto) { this.valorPropuesto = valorPropuesto; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
