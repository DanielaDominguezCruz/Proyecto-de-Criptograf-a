package proyectocriptografia.persistencia;

import proyectocriptografia.dominio.IntentoMuestral;
import proyectocriptografia.dominio.Simulacion;

import java.util.List;

/**
 * Contrato del repositorio de persistencia.
 */
public interface Repositorio {
    Long guardarSimulacion(Simulacion s);
    void actualizarSimulacion(Simulacion s);
    void guardarIntentoMuestral(IntentoMuestral i);
    List<Simulacion> listarSimulaciones();
    List<IntentoMuestral> listarIntentos(long idSimulacion);
}
