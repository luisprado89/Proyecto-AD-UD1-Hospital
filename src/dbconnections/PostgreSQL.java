package dbconnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de conexión a la base de datos PostgreSQL.
 * Gestiona una conexión única y reutilizable para toda la aplicación.
 */
public class PostgreSQL {

    // Parámetros de conexión
    private static final String URL = "jdbc:postgresql://localhost:5432/hospital_postgre";
    private static final String USER = "postgres";
    private static final String PASS = "abc123.";

    // Conexión compartida
    private static Connection connectionPostgres;

    /**
     * Devuelve la conexión activa o crea una nueva si está cerrada.
     */
    public static Connection getConnection() throws SQLException {
        if (connectionPostgres == null || connectionPostgres.isClosed()) {
            connectionPostgres = DriverManager.getConnection(URL, USER, PASS);
            connectionPostgres.setAutoCommit(true);
            System.out.println("🔗 Conexión establecida con PostgreSQL.");
        }
        return connectionPostgres;
    }

    /**
     * Cierra la conexión de forma segura.
     */
    public static void closeConnection() {
        try {
            if (connectionPostgres != null && !connectionPostgres.isClosed()) {
                connectionPostgres.close();
                System.out.println("🔒 Conexión PostgreSQL cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error al cerrar conexión PostgreSQL: " + e.getMessage());
        }
    }
}
