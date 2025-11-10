package proyectocriptografia.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Clase {@code ConexionBD} que proporciona una conexión a la base de datos MySQL
 * utilizada en el proyecto de simulador de ataques.
 * <p>
 * Esta clase usa el patrón Singleton estático, ya que todos los métodos y
 * propiedades son estáticos y no requiere instancias para su uso.
 * </p>
 * 
 * <p><b>Configuración:</b></p>
 * <ul>
 *   <li><b>URL:</b> jdbc:mysql://localhost:3306/simulador_ataques</li>
 *   <li><b>Usuario:</b> root</li>
 *   <li><b>Contraseña:</b> vacía (modificar si tu MySQL tiene contraseña)</li>
 * </ul>
 * 
 * <p><b>Ejemplo de uso:</b></p>
 * <pre>{@code
 * Connection conn = ConexionBD.obtener();
 * }</pre>
 * 
 * <p><b>Notas:</b></p>
 * <ul>
 *   <li>Requiere que el conector MySQL (JAR) esté en el classpath.</li>
 *   <li>Compatible con JDBC 4 o superior.</li>
 * </ul>
 * 
 * @author Tu Nombre
 * @version 1.0
 */
public final class ConexionBD {

    /** URL de conexión a la base de datos MySQL. */
    private static final String URL = 
        "jdbc:mysql://localhost:3306/simulador_ataques?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    /** Nombre de usuario para la conexión a la base de datos. */
    private static final String USUARIO = "root";

    /** Contraseña para la conexión a la base de datos (vacía por defecto). */
    private static final String CLAVE = "";

    /**
     * Constructor privado para evitar instanciación.
     */
    private ConexionBD() {}

    /**
     * Obtiene una conexión activa a la base de datos.
     * 
     * @return un objeto {@link Connection} válido si la conexión es exitosa.
     * @throws IllegalStateException si no se puede establecer la conexión con MySQL.
     */
    public static Connection obtener() {
        try {
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo conectar a MySQL", e);
        }
    }
}
