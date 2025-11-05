package proyectocriptografia.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Proveedor de conexiones JDBC.
 * Ajusta URL/USUARIO/CLAVE según tu instalación XAMPP.
 */
public final class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/simulador_ataques?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CLAVE = ""; // cambia si tienes contraseña

    private ConexionBD(){}

    public static Connection obtener() {
        try {
            // Con JDBC 4+ el driver se auto-registra si el JAR está en el classpath
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo conectar a MySQL", e);
        }
    }
}
