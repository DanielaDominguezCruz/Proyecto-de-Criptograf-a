package dao;

import java.sql.*;

public class SimulacionesDao {

    private final String URL = "jdbc:mysql://localhost:3306/simulador_ataques?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "";

    public String listarSimulaciones() {
        StringBuilder sb = new StringBuilder();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM simulaciones")) {

            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id"))
                  .append(" | Hash: ").append(rs.getString("hash"))
                  .append(" | Resultado: ").append(rs.getString("resultado"))
                  .append("\n");
            }

        } catch (Exception e) {
            sb.append("Error al conectar: ").append(e.getMessage());
        }

        return sb.toString();
    }

    public String obtenerDetalle(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
