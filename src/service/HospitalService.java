package service;

import dbconnections.MySQL;
import dbconnections.PostgreSQL;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio principal del sistema hospitalario.
 * Gestiona toda la lógica de negocio y las operaciones SQL sobre ambas bases de datos.
 */
public class HospitalService {
    private final Connection connectionMySQL;
    private final Connection connectionPostgres;

    // Constructor para abrir las conexiones uina sola vez.
    public HospitalService() {
        try {
            this.connectionMySQL = MySQL.getConnection();
            this.connectionPostgres = PostgreSQL.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con las bases de datos: " + e.getMessage());
        }
    }

    // Cierra ambas conexiones al salir del programa
    public void cerrarConexiones() {
        MySQL.closeConnection();
        PostgreSQL.closeConnection();
    }

    // 1. Crear una nueva especialidad médica (PostgreSQL)
    public void crearEspecialidad(String nombreEspecialidad) {
        String sql = "INSERT INTO hospital.especialidades (nombre_especialidad) VALUES (?)";
        try (PreparedStatement ps = connectionPostgres.prepareStatement(sql)) {
            ps.setString(1, nombreEspecialidad);
            ps.executeUpdate();
            System.out.println("Especialidad creada: " + nombreEspecialidad);
        } catch (SQLException e) {
            System.err.println("Error al crear Especialidad: " + e.getMessage());
        }
    }

    // 2. Crear un nuevo médico (PostgreSQL)
    public void crearMedico(String nombreMedico, String nif, int telefono, String email) {
        String sql = """
                INSERT INTO hospital.medicos (nombre_medico, contacto)
                VALUES (?, ROW(?,?,?,?))
                """;
        try (PreparedStatement ps = connectionPostgres.prepareStatement(sql)) {
            ps.setString(1, nombreMedico);
            ps.setString(2, nombreMedico);
            ps.setString(3, nif);
            ps.setInt(4, telefono);
            ps.setString(5, email);
            ps.executeUpdate();
            System.out.println("Médico creado: " + nombreMedico);
        } catch (SQLException e) {
            System.err.println("Error en crear Médico: " + e.getMessage());
        }
    }

    // 3. Eliminar un médico por ID (PostgreSQL)
    public void eliminarMedico(int id) {
        String sql = "DELETE FROM hospital.medicos WHERE id_medico = ?";
        try (PreparedStatement ps = connectionPostgres.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Médico eliminado con ID: " + id);
            } else {
                System.out.println("No existe médico con ese ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar Médico: " + e.getMessage());
        }
    }

    // 4. Crear un nuevo paciente (MySQL)
    public void crearPaciente(String nombre, String email, LocalDate fechaNacimiento) {
        String sql = "INSERT INTO pacientes (nombre, email, fecha_nacimiento) VALUES (?,?,?)";
        try (PreparedStatement ps = connectionMySQL.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setDate(3, Date.valueOf(fechaNacimiento));
            ps.executeUpdate();
            System.out.println("Paciente creado: " + nombre);
        } catch (SQLException e) {
            System.err.println("Error al crear paciente: " + e.getMessage());
        }
    }

    // 5. Eliminar un paciente (MySQL)
    public void eliminarPaciente(int id) {
        String sql = "DELETE FROM pacientes WHERE id_paciente = ?";
        try (PreparedStatement ps = connectionMySQL.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Paciente eliminado con ID: " + id);
            } else {
                System.out.println("No existe paciente con ese ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error en eliminarPaciente: " + e.getMessage());
        }
    }

    // 6. Crear nuevo tratamiento (nombre, descripcion, especialidad, médico) (MySQL + PostgreSQL)
    public void crearTratamiento(String nombre, String descripcion, String nombreEspecialidad, String nifMedico) {
        String seqSql = "SELECT nextval('hospital.tratamiento_seq') AS id";// obtenemos el siguiente número de la secuencia y lo llamamos 'id'
        String espSql = "SELECT id_especialidad FROM hospital.especialidades WHERE nombre_especialidad = ?";
        String medSql = "SELECT id_medico FROM hospital.medicos WHERE (contacto).nif=?";
        String insPG = "INSERT INTO hospital.tratamientos (id_tratamiento, id_medico, id_especialidad) VALUES (?,?,?)";
        String insMy = "INSERT INTO tratamientos (id_tratamiento, nombre, descripcion) VALUES (?,?,?)";

        try {
            // Desactivar autocommit para manejar transacción manual entre las dos bases
            connectionPostgres.setAutoCommit(false);
            connectionMySQL.setAutoCommit(false);
            // Obtener nuevo ID de la secuencia en PostgreSQL
            int idTratamiento;
            try (Statement st = connectionPostgres.createStatement();
                 ResultSet rs = st.executeQuery(seqSql)) {
                rs.next();
                idTratamiento = rs.getInt("id"); // ← Este es el ID que usarán ambas bases de datos
            }
            //Obtener id de la especialidad según su nombre
            int idEspecialidad;
            try (PreparedStatement ps = connectionPostgres.prepareStatement(espSql)) {
                ps.setString(1, nombreEspecialidad);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) idEspecialidad = rs.getInt(1);
                else throw new SQLException("Especialidad no encontrada: " + nombreEspecialidad);
            }
            //Obtener id del médico según su NIF
            int idMedico;
            try (PreparedStatement ps = connectionPostgres.prepareStatement(medSql)) {
                ps.setString(1, nifMedico);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) idMedico = rs.getInt(1);
                else throw new SQLException("Médico no encontrado con NIF: " + nifMedico);
            }
            //Insertar en PostgeSQL
            // En esta inserción se usa el ID generado en la secuencia (idTratamiento),
            // junto con los IDs del médico y la especialidad.
            try (PreparedStatement ps = connectionPostgres.prepareStatement(insPG)) {
                ps.setInt(1, idTratamiento);
                ps.setInt(2, idMedico);
                ps.setInt(3, idEspecialidad);
                ps.executeUpdate();
            }
            //Insertar en MySQL
            // Aquí se usa exactamente el mismo idTratamiento obtenido de PostgreSQL,
            // para mantener sincronización entre ambas bases de datos.
            try (PreparedStatement ps = connectionMySQL.prepareStatement(insMy)) {
                ps.setInt(1, idTratamiento);
                ps.setString(2, nombre);
                ps.setString(3, descripcion);
                ps.executeUpdate();
            }
            //Confirmar transacciones en ambas bases de datos
            connectionPostgres.commit();
            connectionMySQL.commit();
            System.out.println("Tratamiento creado en ambas BDs con ID " + idTratamiento);
        } catch (SQLException e) {
            System.err.println("Error al crear tratamiento: " + e.getMessage());
            try {
                connectionPostgres.rollback();
            } catch (SQLException ignored) {
            }
            try {
                connectionMySQL.rollback();
            } catch (SQLException ignored) {
            }
        } finally {// Restauramos autocommit
            try {
                connectionPostgres.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
            try {
                connectionMySQL.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    // 7. Eliminar un tratamiento por su nombre (MySQL + PostgreSQL)
    public void eliminarTratamientoPorNombre(String nombre) {
        String sqlSelectId = "SELECT id_tratamiento FROM tratamientos WHERE nombre=?";
        String delMysql = "DELETE FROM tratamientos WHERE id_tratamiento=?";
        String delPostgres = "DELETE FROM hospital.tratamientos WHERE id_tratamiento=?";

        try {
            // Desactivar autocommit para manejar transacción manual entre las dos bases
            connectionPostgres.setAutoCommit(false);
            connectionMySQL.setAutoCommit(false);
            // Obtener el ID del tratamiento a partir del nombre (MySQL)
            int idTratamiento;
            try (PreparedStatement ps = connectionMySQL.prepareStatement(sqlSelectId)) {
                ps.setString(1, nombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idTratamiento = rs.getInt(1);// Tratamiento encontrado
                    } else {//Si no se encuentra, se lanza la excepcion para que el catch maneje el rollback
                        throw new SQLException("No existe tratamiento con el nombre: " + nombre);
                    }
                }
            }
            // Eliminamos el tratamiento en MySQL
            try (PreparedStatement ps = connectionMySQL.prepareStatement(delMysql)) {
                ps.setInt(1, idTratamiento);
                ps.executeUpdate();
            }
            // Eliminamos el tratamiento en PostgreSQL
            try (PreparedStatement ps = connectionPostgres.prepareStatement(delPostgres)) {
                ps.setInt(1, idTratamiento);
                ps.executeUpdate();
            }
            // Confirmamos los cambios en ambas bases de datos
            connectionMySQL.commit();
            connectionPostgres.commit();
            System.out.println("Tratamiento eliminado en ambas BDs (ID: " + idTratamiento + ")");
        } catch (SQLException e) {//Manejamos los errores y revertimos los cambios si algo falla
            System.err.println("Error en eliminarTratamientoPorNombre: " + e.getMessage());
            try {
                connectionMySQL.rollback();
            } catch (SQLException ignored) {
            }
            try {
                connectionPostgres.rollback();
            } catch (SQLException ignored) {
            }
        } finally {//Restauramos el autocommit a su estado normal
            try {
                connectionMySQL.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
            try {
                connectionPostgres.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    // 8. Listar tratamientos (menos de X pacientes asignados) (MySQL)
    public void listarTratamientosConPocosPacientes(int cantidad) {
        String sql = """
            SELECT t.nombre, COUNT(pt.id_paciente) AS pacientes_asignados
            FROM tratamientos t
            LEFT JOIN pacientes_tratamientos pt ON t.id_tratamiento = pt.id_tratamiento
            GROUP BY t.id_tratamiento, t.nombre
            HAVING COUNT(pt.id_paciente) < ?
            ORDER BY pacientes_asignados, t.nombre
            """;
        try (PreparedStatement ps = connectionMySQL.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Tratamientos con menos de " + cantidad + " pacientes:");
                boolean hayResultados = false;

                while (rs.next()) {
                    hayResultados = true;
                    System.out.println(" - " + rs.getString("nombre") +
                            " (" + rs.getInt("pacientes_asignados") + " pacientes)");
                }

                if (!hayResultados) {
                    System.out.println("No existen tratamientos con menos de "
                            + cantidad + " pacientes asignados.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en listarTratamientosConPocosPacientes: " + e.getMessage());
        }
    }


    // 9. Obtener el total de citas realizadas por cada paciente (MySQL)
    public void obtenerTotalCitasPorPaciente() {
        String sql = """
                SELECT p.nombre, COUNT(c.id_cita) AS total_citas
                FROM pacientes p
                LEFT JOIN citas c ON p.id_paciente = c.id_paciente
                GROUP BY p.id_paciente, p.nombre
                ORDER BY p.nombre
                """;
        try (Statement st = connectionMySQL.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("Total de citas por paciente: ");
            while (rs.next()) {
                System.out.println(" - " + rs.getString("nombre") + ": " + rs.getInt("total_citas") + " citas");
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerTotalCitasPorPaciente: " + e.getMessage());
        }
    }

    // 10. Obtener la cantidad de tratamientos por sala (PostgreSQL)
    public void obtenerCantidadTratamientosPorSala() {
        String sql = """
                SELECT s.nombre_sala, COUNT(st.id_tratamiento) AS total
                FROM hospital.salas s
                LEFT JOIN hospital.salas_tratamientos st ON s.id_sala = st.id_sala
                GROUP BY s.id_sala, s.nombre_sala
                ORDER BY s.nombre_sala
                """;
        try (Statement st = connectionPostgres.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("Cantidad de tratamientos por sala: ");
            while (rs.next()) {
                System.out.println(" - " + rs.getString("nombre_sala") + ": " + rs.getInt("total") + " tratamientos");
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerCantidadTratamientosPorSala: " + e.getMessage());
        }
    }

    // 11.  Listar todos los tratamientos con sus respectivas especalidades y médicos (MySQL + PostgreSQL)
    public void listarTratamientosConEspecialidadYMedico() {
        String sqlPostgres = """
                SELECT t.id_tratamiento, e.nombre_especialidad, m.nombre_medico
                FROM hospital.tratamientos t
                JOIN hospital.especialidades e ON t.id_especialidad = e.id_especialidad
                JOIN hospital.medicos m ON t.id_medico = m.id_medico
                ORDER BY t.id_tratamiento
                """;
        String sqlMysql = "SELECT nombre, descripcion FROM tratamientos WHERE id_tratamiento=?";
        try (Statement st = connectionPostgres.createStatement();
             ResultSet rs = st.executeQuery(sqlPostgres)) {
            System.out.println("Tratamientos con su especialidad y médico: ");
            while (rs.next()) {
                int id = rs.getInt("id_tratamiento");
                String especialidad = rs.getString("nombre_especialidad");
                String medico = rs.getString("nombre_medico");
                try (PreparedStatement ps = connectionMySQL.prepareStatement(sqlMysql)) {
                    ps.setInt(1, id);
                    try (ResultSet rm = ps.executeQuery()) {

                        if (rm.next()) {
                            System.out.println("--------------------------------------------------------------------");
                            System.out.println(" - [" + id + "] " + rm.getString("nombre") +
                                    " | " + especialidad + " | " + medico);
                            System.out.println("   Descripción: " + rm.getString("descripcion"));
                        } else {
                            System.out.println(" - [" + id + "] (sin datos en MySQL) | " +
                                    especialidad + " | " + medico);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en listarTratamientosConEspecialidadYMedico: " + e.getMessage());
        }
    }

    // 12. Obtener todos los pacientes que han recibido un tratamiento de una especialidad dada (MySQL + PostgreSQL).
    public void obtenerPacientesPorEspecialidad(int idEspecialidad) {
        String sqlPostgres = "SELECT id_tratamiento FROM hospital.tratamientos WHERE id_especialidad=?";
        String sqlMysql = """
                SELECT DISTINCT p.id_paciente, p.nombre, p.email, p.fecha_nacimiento
                    FROM pacientes p
                    JOIN pacientes_tratamientos pt ON p.id_paciente = pt.id_paciente
                    WHERE pt.id_tratamiento = ?
                """;
        try (PreparedStatement psPg = connectionPostgres.prepareStatement(sqlPostgres)) {
            psPg.setInt(1, idEspecialidad);
            try (ResultSet rsPg = psPg.executeQuery()) {

                List<Integer> ids = new ArrayList<>();
                while (rsPg.next()) {
                    ids.add(rsPg.getInt(1));
                }

                if (ids.isEmpty()) {
                    System.out.println("No hay tratamientos para esa especialidad.");
                    return;
                }

                System.out.println("Pacientes que recibieron tratamientos de la especialidad " + idEspecialidad + ":");

                try (PreparedStatement psMy = connectionMySQL.prepareStatement(sqlMysql)) {
                    for (int idTratamiento : ids) {
                        psMy.setInt(1, idTratamiento);
                        try (ResultSet rsMy = psMy.executeQuery()) {
                            while (rsMy.next()) {
                                System.out.println(" - " + rsMy.getString("nombre")
                                        + " (email: " + rsMy.getString("email")
                                        + ", fecha: " + rsMy.getDate("fecha_nacimiento")
                                        + ") [Tratamiento " + idTratamiento + "]");
                            }
                        }
                    }
                }

            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerPacientesPorEspecialidad: " + e.getMessage());
        }

    }
}
