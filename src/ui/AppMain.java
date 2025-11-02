package ui;

import service.HospitalService;

import java.time.LocalDate;
import java.util.Scanner;

public class AppMain {
    public static void main(String[] args) {
        HospitalService hospitalService = new HospitalService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n============================================");
            System.out.println("        SISTEMA HOSPITALARIO");
            System.out.println("============================================");
            System.out.println("1) Crear una nueva especialidad médica (PostgreSQL)");
            System.out.println("2) Crear un nuevo médico (PostgreSQL)");
            System.out.println("3) Eliminar un médico por ID (PostgreSQL)");
            System.out.println("4) Crear un nuevo paciente (MySQL)");
            System.out.println("5) Eliminar un paciente (MySQL)");
            System.out.println("6) Crear nuevo tratamiento (MySQL + PostgreSQL)");
            System.out.println("7) Eliminar un tratamiento por su nombre (MySQL + PostgreSQL)");
            System.out.println("8) Listar tratamientos (menos de X pacientes asignados) (MySQL)");
            System.out.println("9) Obtener el total de citas realizadas por cada paciente (MySQL)");
            System.out.println("10) Obtener la cantidad de tratamientos por sala (PostgreSQL)");
            System.out.println("11) Listar todos los tratamientos con sus respectivas especalidades y médicos (MySQL + PostgreSQL)");
            System.out.println("12) Obtener todos los pacientes que han recibido un tratamiento de una especialidad dada (MySQL + PostgreSQL).");
            System.out.println("0) Salir");
            System.out.println("-----------------------------------------");
            System.out.print("Seleccione una opción: ");
            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Opción no válida.");
                continue;
            }
            try {
                switch (opcion) {
                    case 1 -> {
                        System.out.print("Nombre de la especialidad: ");
                        String nombreEspecialidad = scanner.nextLine().trim();
                        hospitalService.crearEspecialidad(nombreEspecialidad);
                    }
                    case 2 -> {
                        System.out.print("Nombre del Médico: ");
                        String nombreMedico = scanner.nextLine().trim();
                        System.out.print("NIF: ");
                        String nif = scanner.nextLine().trim();
                        System.out.print("Teléfono: ");
                        int telefono = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Email: ");
                        String email = scanner.nextLine().trim();
                        hospitalService.crearMedico(nombreMedico, nif, telefono, email);
                    }
                    case 3 -> {
                        System.out.print("ID del médico: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        hospitalService.eliminarMedico(id);
                    }
                    case 4 -> {
                        System.out.print("Nombre del paciente: ");
                        String nombre = scanner.nextLine().trim();
                        System.out.print("Email: ");
                        String email = scanner.nextLine().trim();
                        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
                        LocalDate fechaNacimiento = LocalDate.parse(scanner.nextLine().trim());
                        hospitalService.crearPaciente(nombre, email, fechaNacimiento);
                    }
                    case 5 -> {
                        System.out.print("ID del paciente: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        hospitalService.eliminarPaciente(id);
                    }
                    case 6 -> {
                        System.out.print("Nombre del tratamiento: ");
                        String nombre = scanner.nextLine().trim();
                        System.out.print("Descripción: ");
                        String descripcion = scanner.nextLine().trim();
                        System.out.print("Nombre de la especialidad: ");
                        String nombreEspecialidad = scanner.nextLine().trim();
                        System.out.print("NIF del médico: ");
                        String nifMedico = scanner.nextLine().trim();
                        hospitalService.crearTratamiento(nombre, descripcion, nombreEspecialidad, nifMedico);
                    }
                    case 7 -> {
                        System.out.print("Nombre del tratamiento: ");
                        String nombre = scanner.nextLine().trim();
                        hospitalService.eliminarTratamientoPorNombre(nombre);
                    }
                    case 8 -> {
                        System.out.print("Máximo de pacientes asignados: ");
                        int cantidad = Integer.parseInt(scanner.nextLine().trim());
                        hospitalService.listarTratamientosConPocosPacientes(cantidad);
                    }
                    case 9 -> hospitalService.obtenerTotalCitasPorPaciente();
                    case 10 -> hospitalService.obtenerCantidadTratamientosPorSala();
                    case 11 -> hospitalService.listarTratamientosConEspecialidadYMedico();
                    case 12 -> {
                        System.out.print("ID de la especialidad (PostgreSQL): ");
                        int idEspecialidad = Integer.parseInt(scanner.nextLine().trim());
                        hospitalService.obtenerPacientesPorEspecialidad(idEspecialidad);
                    }
                    case 0 -> {
                        System.out.println("Fin del programa. ¡Hasta pronto!");
                        hospitalService.cerrarConexiones(); // Cerramos las conexiones
                        return;
                    }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.err.println("Error al ejecutar la opción: " + e.getMessage());
            }
        }
    }
}
