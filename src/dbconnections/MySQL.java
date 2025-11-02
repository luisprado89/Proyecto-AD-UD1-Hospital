package dbconnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de conexión a la base de datos MySQL.
 * Gestiona una única conexión reutilizable durante la ejecución del programa.
 */
public class MySQL {

    // Parámetros de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_mysql?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "abc123.";

    // Conexión compartida
    private static Connection connectionMySQL;

    /**
     * Devuelve la conexión activa o crea una nueva si está cerrada.
     */
    public static Connection getConnection() throws SQLException {
        if (connectionMySQL == null || connectionMySQL.isClosed()) {
            connectionMySQL = DriverManager.getConnection(URL, USER, PASS);
            connectionMySQL.setAutoCommit(true);
            System.out.println("🔗 Conexión establecida con MySQL.");
        }
        return connectionMySQL;
    }

    /**
     * Cierra la conexión de forma segura.
     */
    public static void closeConnection() {
        try {
            if (connectionMySQL != null && !connectionMySQL.isClosed()) {
                connectionMySQL.close();
                System.out.println("🔒 Conexión MySQL cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error al cerrar conexión MySQL: " + e.getMessage());
        }
    }
}
