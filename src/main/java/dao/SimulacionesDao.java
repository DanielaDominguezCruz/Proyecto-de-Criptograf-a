package dao;

import java.sql.*;

/**
 * DAO para operaciones con la tabla simulacion.
 * Conecta a la BD y realiza consultas de listado y detalle.
 */
public class SimulacionesDao {

    private final String URL = "jdbc:mysql://localhost:3306/simulador_ataques?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "";

    /**
     * Lista todas las simulaciones registradas en la tabla simulacion.
     * @return texto formateado con los resultados o mensaje de error.
     */
    public String listarSimulaciones() {
        StringBuilder sb = new StringBuilder();

        String query = "SELECT id, tipo, objetivo_hash, clave_hallada, intentos_totales, inicio, fin " +
                       "FROM simulacion";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id")).append("\n")
                  .append("Tipo: ").append(rs.getString("tipo")).append("\n")
                  .append("Hash objetivo: ").append(rs.getString("objetivo_hash")).append("\n")
                  .append("Clave hallada: ").append(rs.getString("clave_hallada")).append("\n")
                  .append("Intentos: ").append(rs.getLong("intentos_totales")).append("\n")
                  .append("Inicio: ").append(rs.getTimestamp("inicio")).append("\n")
                  .append("Fin: ").append(rs.getTimestamp("fin")).append("\n")
                  .append("-------------------------------------\n");
            }

        } catch (Exception e) {
            sb.append("Error al conectar: ").append(e.getMessage());
        }

        return sb.toString();
    }

    /**
     * Obtiene el detalle de una simulación por ID.
     * @param idSim string con el ID numérico.
     * @return detalle formateado o error.
     */
    public String obtenerDetalle(String idSim) {
        StringBuilder sb = new StringBuilder();
        String query = "SELECT * FROM simulacion WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, Integer.parseInt(idSim));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id")).append("\n")
                  .append("Tipo: ").append(rs.getString("tipo")).append("\n")
                  .append("Hash objetivo: ").append(rs.getString("objetivo_hash")).append("\n")
                  .append("Parámetros:\n").append(rs.getString("parametros")).append("\n\n")
                  .append("Clave hallada: ").append(rs.getString("clave_hallada")).append("\n")
                  .append("Intentos: ").append(rs.getLong("intentos_totales")).append("\n")
                  .append("Intentos por segundo: ").append(rs.getDouble("intentos_por_segundo")).append("\n")
                  .append("Éxito: ").append(rs.getBoolean("exito")).append("\n")
                  .append("Inicio: ").append(rs.getTimestamp("inicio")).append("\n")
                  .append("Fin: ").append(rs.getTimestamp("fin")).append("\n");
            } else {
                sb.append("No se encontró la simulación con ID ").append(idSim);
            }

        } catch (Exception e) {
            sb.append("Error: ").append(e.getMessage());
        }

        return sb.toString();
    }
}
