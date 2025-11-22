package modelo;

/**
 * Representa una simulación realizada por el sistema.
 */
public class Simulacion {

    private int id;
    private String tipo;
    private String objetivoHash;
    private String claveHallada;
    private long intentosTotales;
    private double intentosPorSegundo;
    private long duracionMs;
    private boolean exito;

    /**
     * Crea una nueva instancia de Simulacion.
     *
     * @param id identificador de la simulación
     * @param tipo tipo de ataque realizado
     * @param objetivoHash hash objetivo utilizado en la simulación
     * @param claveHallada clave encontrada durante la simulación
     * @param intentosTotales total de intentos realizados
     * @param intentosPorSegundo promedio de intentos por segundo
     * @param duracionMs duración total en milisegundos
     * @param exito indica si la simulación tuvo éxito
     */
    public Simulacion(
            int id,
            String tipo,
            String objetivoHash,
            String claveHallada,
            long intentosTotales,
            double intentosPorSegundo,
            long duracionMs,
            boolean exito
    ) {
        this.id = id;
        this.tipo = tipo;
        this.objetivoHash = objetivoHash;
        this.claveHallada = claveHallada;
        this.intentosTotales = intentosTotales;
        this.intentosPorSegundo = intentosPorSegundo;
        this.duracionMs = duracionMs;
        this.exito = exito;
    }

    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public String getObjetivoHash() { return objetivoHash; }
    public String getClaveHallada() { return claveHallada; }
    public long getIntentosTotales() { return intentosTotales; }
    public double getIntentosPorSegundo() { return intentosPorSegundo; }
    public long getDuracion() { return duracionMs; }
    public boolean isExito() { return exito; }
}
