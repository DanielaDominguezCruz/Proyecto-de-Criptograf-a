package proyectocriptografia.dominio;

import java.time.Instant;

/**
 * Representa una ejecución de simulación de ataque.
 */
public class Simulacion {
    private Long id;
    private String tipo;
    private String objetivoHash;
    private String parametros;
    private Instant inicio;
    private Instant fin;
    private boolean exito;
    private String claveHallada;
    private long intentosTotales;
    private double intentosPorSegundo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getObjetivoHash() { return objetivoHash; }
    public void setObjetivoHash(String objetivoHash) { this.objetivoHash = objetivoHash; }
    public String getParametros() { return parametros; }
    public void setParametros(String parametros) { this.parametros = parametros; }
    public Instant getInicio() { return inicio; }
    public void setInicio(Instant inicio) { this.inicio = inicio; }
    public Instant getFin() { return fin; }
    public void setFin(Instant fin) { this.fin = fin; }
    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }
    public String getClaveHallada() { return claveHallada; }
    public void setClaveHallada(String claveHallada) { this.claveHallada = claveHallada; }
    public long getIntentosTotales() { return intentosTotales; }
    public void setIntentosTotales(long intentosTotales) { this.intentosTotales = intentosTotales; }
    public double getIntentosPorSegundo() { return intentosPorSegundo; }
    public void setIntentosPorSegundo(double intentosPorSegundo) { this.intentosPorSegundo = intentosPorSegundo; }

    @Override public String toString() {
        return "Simulacion{id=%d,tipo=%s,exito=%b,intentos=%d,ips=%.2f}".formatted(
                id,tipo,exito,intentosTotales,intentosPorSegundo);
    }
}
