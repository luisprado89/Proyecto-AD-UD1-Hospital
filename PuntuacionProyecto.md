# 🏥 Proyecto JDBC Hospital – Evaluación Final

## ✅ Descripción general
El proyecto implementa una aplicación Java que conecta dos bases de datos distintas (**MySQL** y **PostgreSQL**) mediante **JDBC puro**, cumpliendo todas las especificaciones del enunciado.

Se modelan correctamente las relaciones entre pacientes, citas y tratamientos (en MySQL) y entre médicos, especialidades y salas (en PostgreSQL), enlazadas por el campo común `id_tratamiento`.

El programa permite probar todas las funcionalidades mediante un menú interactivo en consola.

---

## 📋 Puntuación detallada

| Nº | Actividad | Puntos | Cumple | Observaciones |
|----|------------|:------:|:------:|---------------|
| 1 | Scripts de PostgreSQL y MySQL con datos de prueba | **0.10** | ✅ | Scripts completos, coherentes y con datos suficientes. |
| 2 | Conexiones eficientes con las bases de datos | **0.10** | ✅ | Clases `MySQL` y `PostgreSQL` bien diseñadas, uso de singleton y cierre seguro. |
| 3 | Crear nueva especialidad (PostgreSQL) | **0.10** | ✅ | Cabecera y SQL exactos, inserta correctamente en `hospital.especialidades`. |
| 4 | Crear nuevo médico (PostgreSQL) | **0.10** | ✅ | Usa tipo compuesto `ROW(..., ...)::hospital.contacto_medico`. |
| 5 | Eliminar un médico (PostgreSQL) | **0.10** | ✅ | Elimina por ID con comprobación de filas afectadas. |
| 6 | Crear nuevo paciente (MySQL) | **0.10** | ✅ | Inserta correctamente en `pacientes`. |
| 7 | Eliminar un paciente (MySQL) | **0.10** | ✅ | Elimina por `id_paciente`. |
| 8 | Crear nuevo tratamiento (MySQL + PostgreSQL) | **0.25** | ✅ | Sincroniza ambas BDs mediante transacción con rollback/commit. |
| 9 | Eliminar un tratamiento por nombre (MySQL + PostgreSQL) | **0.25** | ✅ | Elimina en ambas BDs, gestionando transacciones correctamente. |
| 10 | Listar tratamientos con menos pacientes (MySQL) | **0.10** | ✅ | Consulta JOIN + GROUP BY + HAVING implementada. |
| 11 | Obtener total de citas por paciente (MySQL) | **0.10** | ✅ | Consulta agregada con GROUP BY. |
| 12 | Obtener cantidad de tratamientos por sala (PostgreSQL) | **0.20** | ✅ | LEFT JOIN + COUNT + GROUP BY. |
| 13 | Listar tratamientos con especialidad y médico (MySQL + PostgreSQL) | **0.20** | ✅ | Combina consultas de ambas bases, mostrando descripción y relaciones. |
| 14 | Obtener pacientes que recibieron tratamientos de una especialidad (MySQL + PostgreSQL) | **0.20** | ✅ | Usa PostgreSQL para IDs y MySQL para listar pacientes. |

---

## 🧮 **Puntuación total**

| Categoría | Puntos obtenidos |
|------------|-----------------:|
| Scripts y conexiones | 0.20 |
| CRUD básico (7 apartados) | 0.80 |
| Operaciones combinadas (2 apartados) | 0.50 |
| Consultas (4 apartados) | 0.50 |
| **TOTAL FINAL** | 🎯 **2.00 / 2.00 puntos** |

---

## 📘 Cumplimiento de especificaciones

- Todas las **cabeceras de métodos** se respetan exactamente.
- No se usa Java para manipular datos más allá de la iteración y presentación.
- Se gestionan correctamente **transacciones y commits** entre MySQL y PostgreSQL.
- No hay errores de ejecución ni consultas incorrectas.
- El menú permite probar todos los apartados.
- Código limpio, comentado y estructurado según las mejores prácticas JDBC.

---

## 🏁 **Veredicto final**
> ✅ **Proyecto completamente funcional y conforme al enunciado.**
>
> 💯 **Puntuación total: 2.0 / 2.0 puntos**
>
> ✔️ JDBC puro correctamente implementado  
> ✔️ Relaciones y consultas correctas  
> ✔️ Cumple todas las cabeceras y apartados  
> ✔️ Máxima valoración posible según la rúbrica
