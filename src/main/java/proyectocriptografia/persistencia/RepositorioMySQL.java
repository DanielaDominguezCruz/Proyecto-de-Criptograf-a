package proyectocriptografia.persistencia;

import proyectocriptografia.dominio.IntentoMuestral;
import proyectocriptografia.dominio.Simulacion;

import java.sql.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion JDBC del repositorio.
 */
public class RepositorioMySQL implements Repositorio {
    private static final DateTimeFormatter F =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

    @Override
    public Long guardarSimulacion(Simulacion s) {
        String sql = "INSERT INTO simulacion(tipo, objetivo_hash, parametros, inicio, fin, exito, clave_hallada, intentos_totales, intentos_por_segundo) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getTipo());
            ps.setString(2, s.getObjetivoHash());
            ps.setString(3, s.getParametros());
            ps.setString(4, s.getInicio()==null? null : F.format(s.getInicio()));
            ps.setString(5, s.getFin()==null? null : F.format(s.getFin()));
            ps.setBoolean(6, s.isExito());
            ps.setString(7, s.getClaveHallada());
            ps.setLong(8, s.getIntentosTotales());
            ps.setDouble(9, s.getIntentosPorSegundo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new IllegalStateException("No se pudo obtener id generado");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al guardar simulacion", e);
        }
    }

    @Override
    public void actualizarSimulacion(Simulacion s) {
        String sql = "UPDATE simulacion SET fin=?, exito=?, clave_hallada=?, intentos_totales=?, intentos_por_segundo=? WHERE id=?";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getFin()==null? null : F.format(s.getFin()));
            ps.setBoolean(2, s.isExito());
            ps.setString(3, s.getClaveHallada());
            ps.setLong(4, s.getIntentosTotales());
            ps.setDouble(5, s.getIntentosPorSegundo());
            ps.setLong(6, s.getId());
            ps.executeUpdate();
        } catch (SQLException e){
            throw new IllegalStateException("Error al actualizar simulacion", e);
        }
    }

    @Override
    public void guardarIntentoMuestral(IntentoMuestral i) {
        String sql = "INSERT INTO intento_muestral(id_simulacion, indice, valor_propuesto, timestamp) VALUES (?,?,?,?)";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, i.getIdSimulacion());
            ps.setLong(2, i.getIndice());
            ps.setString(3, i.getValorPropuesto());
            ps.setString(4, F.format(i.getTimestamp()));
            ps.executeUpdate();
        } catch (SQLException e){
            throw new IllegalStateException("Error al guardar intento muestral", e);
        }
    }

    @Override
    public List<Simulacion> listarSimulaciones() {
        List<Simulacion> out = new ArrayList<>();
        String sql = "SELECT id, tipo, objetivo_hash, parametros, inicio, fin, exito, clave_hallada, intentos_totales, intentos_por_segundo FROM simulacion ORDER BY id DESC";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Simulacion s = new Simulacion();
                s.setId(rs.getLong("id"));
                s.setTipo(rs.getString("tipo"));
                s.setObjetivoHash(rs.getString("objetivo_hash"));
                s.setParametros(rs.getString("parametros"));
                s.setInicio(rs.getTimestamp("inicio")==null? null : rs.getTimestamp("inicio").toInstant());
                s.setFin(rs.getTimestamp("fin")==null? null : rs.getTimestamp("fin").toInstant());
                s.setExito(rs.getBoolean("exito"));
                s.setClaveHallada(rs.getString("clave_hallada"));
                s.setIntentosTotales(rs.getLong("intentos_totales"));
                s.setIntentosPorSegundo(rs.getDouble("intentos_por_segundo"));
                out.add(s);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar simulaciones", e);
        }
        return out;
    }

    @Override
    public List<IntentoMuestral> listarIntentos(long idSimulacion) {
        List<IntentoMuestral> out = new ArrayList<>();
        String sql = "SELECT id, id_simulacion, indice, valor_propuesto, timestamp FROM intento_muestral WHERE id_simulacion = ? ORDER BY indice ASC";
        try (Connection c = ConexionBD.obtener();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idSimulacion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    IntentoMuestral i = new IntentoMuestral();
                    i.setId(rs.getLong("id"));
                    i.setIdSimulacion(rs.getLong("id_simulacion"));
                    i.setIndice(rs.getLong("indice"));
                    i.setValorPropuesto(rs.getString("valor_propuesto"));
                    i.setTimestamp(rs.getTimestamp("timestamp").toInstant());
                    out.add(i);
                }
            }
        } catch (SQLException e){
            throw new IllegalStateException("Error al listar intentos", e);
        }
        return out;
    }
}
