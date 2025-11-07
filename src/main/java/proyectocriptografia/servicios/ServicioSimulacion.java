package proyectocriptografia.servicios;

import proyectocriptografia.ataques.*;
import proyectocriptografia.dominio.IntentoMuestral;
import proyectocriptografia.dominio.Simulacion;
import proyectocriptografia.persistencia.Repositorio;
import proyectocriptografia.persistencia.RepositorioMySQL;
import proyectocriptografia.util.Medidor;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Servicio que gestiona la ejecucion de simulaciones de ataques criptograficos.
 * Configura, ejecuta, mide y persiste los resultados de las simulaciones.
 */
public class ServicioSimulacion {

    private final Repositorio repo;

    /**
     * Constructor por defecto. Inicializa el repositorio MySQL.
     */
    public ServicioSimulacion(){
        this.repo = new RepositorioMySQL();
    }

    /**
     * Constructor alternativo para pruebas, permite inyectar un repositorio personalizado.
     * @param repo implementacion de Repositorio a utilizar
     */
    public ServicioSimulacion(Repositorio repo){ this.repo = repo; }

    /**
     * Ejecuta una simulacion de fuerza bruta. Mide el rendimiento y guarda muestras periodicas.
     * @param p parametros de configuracion del ataque
     * @return objeto Simulacion con los resultados obtenidos
     */
    public Simulacion simularFuerzaBruta(ParametrosAtaque p) {
        Simulacion s = crearSimulacionBase("FUERZA_BRUTA", p);
        Long id = repo.guardarSimulacion(s);
        s.setId(id);

        AtaqueFuerzaBruta ataque = new AtaqueFuerzaBruta();
        ataque.configurar(p);

        long inicio = System.currentTimeMillis();
        ataque.iniciar();

        waitForStart(ataque, 200);

        long ultimoMuestreo = 0;
        long pollingMs = 50;
        while (ataque.estaEjecutando()) {
            dormir(pollingMs);
            long it = ataque.getIntentosRealizados();
            if (p.maxIntentosMuestralesCada > 0) {
                long muestraIndex = it / p.maxIntentosMuestralesCada;
                if (muestraIndex > ultimoMuestreo) {
                    ultimoMuestreo = muestraIndex;
                    guardarMuestra(id, it, "<muestra-fb-" + it + ">");
                }
            }
        }

        long itFinal = ataque.getIntentosRealizados();
        if (p.maxIntentosMuestralesCada <= 0 || itFinal == 0 || itFinal % p.maxIntentosMuestralesCada != 0) {
            guardarMuestra(id, itFinal, "<muestra-fb-final-" + itFinal + ">");
        }

        long fin = System.currentTimeMillis();
        completarYActualizar(s, ataque, inicio, fin);
        return s;
    }

    /**
     * Ejecuta una simulacion de diccionario. Mide el rendimiento y guarda muestras periodicas.
     * @param p parametros de configuracion del ataque
     * @return objeto Simulacion con los resultados obtenidos
     */
    public Simulacion simularDiccionario(ParametrosAtaque p) {
        Simulacion s = crearSimulacionBase("DICCIONARIO", p);
        Long id = repo.guardarSimulacion(s);
        s.setId(id);

        AtaqueDiccionario ataque = new AtaqueDiccionario();
        ataque.configurar(p);

        long inicio = System.currentTimeMillis();
        ataque.iniciar();

        waitForStart(ataque, 200);

        long ultimoMuestreo = 0;
        long pollingMs = 50;
        while (ataque.estaEjecutando()) {
            dormir(pollingMs);
            long it = ataque.getIntentosRealizados();
            if (p.maxIntentosMuestralesCada > 0) {
                long muestraIndex = it / p.maxIntentosMuestralesCada;
                if (muestraIndex > ultimoMuestreo) {
                    ultimoMuestreo = muestraIndex;
                    guardarMuestra(id, it, "<muestra-dic-" + it + ">");
                }
            }
        }

        long itFinal = ataque.getIntentosRealizados();
        if (p.maxIntentosMuestralesCada <= 0 || itFinal == 0 || itFinal % p.maxIntentosMuestralesCada != 0) {
            String valor = ataque.getResultadoClave() != null ? ataque.getResultadoClave() : "<muestra-dic-final-" + itFinal + ">";
            guardarMuestra(id, itFinal, valor);
        }

        long fin = System.currentTimeMillis();
        completarYActualizar(s, ataque, inicio, fin);
        return s;
    }

    /**
     * Espera hasta que el ataque entre en estado de ejecucion o se alcance el tiempo maximo.
     * @param ataque instancia del ataque a monitorear
     * @param maxWaitMs tiempo maximo de espera en milisegundos
     */
    private void waitForStart(Ataque ataque, long maxWaitMs) {
        long t0 = System.currentTimeMillis();
        while (!ataque.estaEjecutando() && System.currentTimeMillis() - t0 < maxWaitMs) {
            dormir(20);
        }
        if (!ataque.estaEjecutando()) {
            System.out.println("DEBUG: el ataque no entro en estado ejecutando en " + maxWaitMs + "ms. Continuo el control de ciclo.");
        }
    }

    /**
     * Completa los datos finales de la simulacion y actualiza su registro en el repositorio.
     * @param s simulacion a actualizar
     * @param ataque instancia del ataque ejecutado
     * @param inicio marca de tiempo de inicio
     * @param fin marca de tiempo de finalizacion
     */
    private void completarYActualizar(Simulacion s, Ataque ataque, long inicio, long fin){
        s.setFin(Instant.ofEpochMilli(fin));
        s.setIntentosTotales(ataque.getIntentosRealizados());
        s.setIntentosPorSegundo(Medidor.intentosPorSegundo(s.getIntentosTotales(), (fin - inicio)));
        s.setExito(ataque.getResultadoClave()!=null);
        s.setClaveHallada(ataque.getResultadoClave());
        repo.actualizarSimulacion(s);
    }

    /**
     * Guarda un intento muestral asociado a una simulacion.
     * @param idSim identificador de la simulacion
     * @param indice numero de intento
     * @param valor valor propuesto en el intento
     */
    private void guardarMuestra(Long idSim, long indice, String valor){
        try {
            IntentoMuestral im = new IntentoMuestral();
            im.setIdSimulacion(idSim);
            im.setIndice(indice);
            im.setValorPropuesto(valor);
            im.setTimestamp(Instant.now());
            repo.guardarIntentoMuestral(im);
        } catch (Exception e) {
            System.out.println("WARN: no se pudo guardar muestra: " + e.getMessage());
        }
    }

    /**
     * Crea un objeto Simulacion basico inicializado con tipo, parametros y hora de inicio.
     * @param tipo tipo de simulacion (ej. FUERZA_BRUTA o DICCIONARIO)
     * @param p parametros del ataque
     * @return instancia inicial de Simulacion
     */
    private static Simulacion crearSimulacionBase(String tipo, ParametrosAtaque p){
        Simulacion s = new Simulacion();
        s.setTipo(tipo);
        s.setObjetivoHash(p.hashObjetivo);
        s.setParametros(p.toResumen());
        s.setInicio(Instant.now());
        return s;
    }

    /**
     * Suspende la ejecucion del hilo actual por el tiempo indicado.
     * @param ms milisegundos de espera
     */
    private static void dormir(long ms){
        try { TimeUnit.MILLISECONDS.sleep(ms); } catch (InterruptedException ignored){}
    }

    /**
     * Consulta la lista de simulaciones almacenadas en el repositorio.
     * @return lista de simulaciones registradas
     */
    public List<Simulacion> consultarSimulaciones(){ return repo.listarSimulaciones(); }

    /**
     * Consulta los intentos muestrales asociados a una simulacion.
     * @param idSimulacion identificador de la simulacion
     * @return lista de intentos muestrales registrados
     */
    public List<IntentoMuestral> consultarDetalle(long idSimulacion){ return repo.listarIntentos(idSimulacion); }
}
