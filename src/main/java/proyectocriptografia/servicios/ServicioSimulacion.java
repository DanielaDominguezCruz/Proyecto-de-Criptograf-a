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
 * Orquestador principal de simulaciones: configura, ejecuta, mide y persiste.
 * Esta versión es más robusta: detecta si el ataque no arranca, fuerza un muestreo
 * final y registra resultados aunque el ataque haya terminado rápidamente.
 */
public class ServicioSimulacion {

    private final Repositorio repo;

    public ServicioSimulacion(){
        this.repo = new RepositorioMySQL();
    }
    // Constructor alternativo para tests
    public ServicioSimulacion(Repositorio repo){ this.repo = repo; }

    /**
     * Ejecuta una simulación de fuerza bruta, muestreando periódicamente y guardando la simulación.
     */
    public Simulacion simularFuerzaBruta(ParametrosAtaque p) {
        Simulacion s = crearSimulacionBase("FUERZA_BRUTA", p);
        Long id = repo.guardarSimulacion(s);
        s.setId(id);

        AtaqueFuerzaBruta ataque = new AtaqueFuerzaBruta();
        ataque.configurar(p);

        long inicio = System.currentTimeMillis();
        ataque.iniciar();

        // Espera corta para comprobar que arranque (evita bucle infinito si no arrancó)
        waitForStart(ataque, 200);

        long ultimoMuestreo = 0;
        long pollingMs = 50;
        while (ataque.estaEjecutando()) {
            dormir(pollingMs);
            long it = ataque.getIntentosRealizados();
            // muestreo basado en "cada N intentos"
            if (p.maxIntentosMuestralesCada > 0) {
                long muestraIndex = it / p.maxIntentosMuestralesCada;
                if (muestraIndex > ultimoMuestreo) {
                    ultimoMuestreo = muestraIndex;
                    guardarMuestra(id, it, "<muestra-fb-" + it + ">");
                }
            }
        }

        // muestreo final: siempre guarda el último estado (si no se guardó nada antes)
        long itFinal = ataque.getIntentosRealizados();
        if (p.maxIntentosMuestralesCada <= 0 || itFinal == 0 || itFinal % p.maxIntentosMuestralesCada != 0) {
            guardarMuestra(id, itFinal, "<muestra-fb-final-" + itFinal + ">");
        }

        long fin = System.currentTimeMillis();
        completarYActualizar(s, ataque, inicio, fin);
        return s; // devuelve lectura actualizada
    }

    /**
     * Ejecuta una simulación de diccionario, muestreando periódicamente y guardando la simulación.
     */
    public Simulacion simularDiccionario(ParametrosAtaque p) {
        Simulacion s = crearSimulacionBase("DICCIONARIO", p);
        Long id = repo.guardarSimulacion(s);
        s.setId(id);

        AtaqueDiccionario ataque = new AtaqueDiccionario();
        ataque.configurar(p);

        long inicio = System.currentTimeMillis();
        ataque.iniciar();

        // Espera corta para comprobar que arranque
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

        // muestreo final: guarda la última iteración (para que intentos_totales no quede 0)
        long itFinal = ataque.getIntentosRealizados();
        if (p.maxIntentosMuestralesCada <= 0 || itFinal == 0 || itFinal % p.maxIntentosMuestralesCada != 0) {
            // si el ataque encontró la clave, la valoramos en el muestreo final
            String valor = ataque.getResultadoClave() != null ? ataque.getResultadoClave() : "<muestra-dic-final-" + itFinal + ">";
            guardarMuestra(id, itFinal, valor);
        }

        long fin = System.currentTimeMillis();
        completarYActualizar(s, ataque, inicio, fin);
        return s;
    }

    /**
     * Si el ataque no entra en estado 'ejecutando' en un lapso corto, lo avisamos.
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

    private void completarYActualizar(Simulacion s, Ataque ataque, long inicio, long fin){
        s.setFin(Instant.ofEpochMilli(fin));
        s.setIntentosTotales(ataque.getIntentosRealizados());
        s.setIntentosPorSegundo(Medidor.intentosPorSegundo(s.getIntentosTotales(), (fin - inicio)));
        s.setExito(ataque.getResultadoClave()!=null);
        s.setClaveHallada(ataque.getResultadoClave());
        repo.actualizarSimulacion(s);
    }

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

    private static Simulacion crearSimulacionBase(String tipo, ParametrosAtaque p){
        Simulacion s = new Simulacion();
        s.setTipo(tipo);
        s.setObjetivoHash(p.hashObjetivo);
        s.setParametros(p.toResumen());
        s.setInicio(Instant.now());
        return s;
    }

    private static void dormir(long ms){
        try { TimeUnit.MILLISECONDS.sleep(ms); } catch (InterruptedException ignored){}
    }

    public List<Simulacion> consultarSimulaciones(){ return repo.listarSimulaciones(); }
    public List<IntentoMuestral> consultarDetalle(long idSimulacion){ return repo.listarIntentos(idSimulacion); }
}
