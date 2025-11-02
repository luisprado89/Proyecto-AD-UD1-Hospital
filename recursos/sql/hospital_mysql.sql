-- ============================================================
--  BASE DE DATOS MYSQL: hospital_mysql
--  Codificación: UTF-8
-- ============================================================

DROP DATABASE IF EXISTS hospital_mysql;
CREATE DATABASE hospital_mysql CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE hospital_mysql;
SET NAMES utf8mb4;

-- ============================================================
--  TABLAS
-- ============================================================

CREATE TABLE pacientes (
  id_paciente INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(100),
  fecha_nacimiento DATE
);

CREATE TABLE tratamientos (
  id_tratamiento INT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT
);

CREATE TABLE pacientes_tratamientos (
  id_paciente INT,
  id_tratamiento INT,
  fecha_inicio DATE,
  PRIMARY KEY (id_paciente, id_tratamiento),
  FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente)
    ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_tratamiento) REFERENCES tratamientos(id_tratamiento)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE citas (
  id_cita INT AUTO_INCREMENT PRIMARY KEY,
  id_paciente INT,
  fecha DATE,
  FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente)
    ON DELETE CASCADE ON UPDATE CASCADE
);


-- ============================================================
--  INSERCIÓN DE DATOS DE EJEMPLO
-- ============================================================

-- ============================================================
--  PACIENTES (40)
-- ============================================================

INSERT INTO pacientes (nombre, email, fecha_nacimiento) VALUES
('Ana Pérez', 'ana.perez@gmail.com', '1990-05-12'),
('Luis Gómez', 'luis.gomez@gmail.com', '1985-10-23'),
('María López', 'maria.lopez@gmail.com', '2000-03-15'),
('Carlos Rodríguez', 'carlos.rodriguez@gmail.com', '1988-07-09'),
('Laura Fernández', 'laura.fernandez@gmail.com', '1995-11-02'),
('Jorge Castro', 'jorge.castro@gmail.com', '1992-04-17'),
('Lucía Méndez', 'lucia.mendez@gmail.com', '1998-02-28'),
('Andrés Torres', 'andres.torres@gmail.com', '1993-09-20'),
('Elena Ruiz', 'elena.ruiz@gmail.com', '1986-01-13'),
('Manuel Iglesias', 'manuel.iglesias@gmail.com', '1999-06-25'),
('Beatriz Ramos', 'beatriz.ramos@gmail.com', '1987-09-12'),
('Raúl Sánchez', 'raul.sanchez@gmail.com', '1991-02-05'),
('Patricia León', 'patricia.leon@gmail.com', '1993-12-01'),
('Tomás Gutiérrez', 'tomas.gutierrez@gmail.com', '1989-08-14'),
('Carmen Vega', 'carmen.vega@gmail.com', '1996-04-22'),
('Pablo Navarro', 'pablo.navarro@gmail.com', '1994-11-10'),
('Isabel Domínguez', 'isabel.dominguez@gmail.com', '1992-07-18'),
('Rubén Morales', 'ruben.morales@gmail.com', '1988-01-03'),
('Rosa Castro', 'rosa.castro@gmail.com', '1999-09-27'),
('Pedro Herrera', 'pedro.herrera@gmail.com', '1985-06-16'),
('Silvia Vidal', 'silvia.vidal@gmail.com', '1997-12-20'),
('Fernando Alonso', 'fernando.alonso@gmail.com', '1984-03-28'),
('Clara López', 'clara.lopez@gmail.com', '2001-02-14'),
('Esteban Cano', 'esteban.cano@gmail.com', '1990-10-09'),
('Alicia Núñez', 'alicia.nunez@gmail.com', '1998-11-03'),
('Daniel Ortega', 'daniel.ortega@gmail.com', '1995-01-25'),
('Teresa Rivas', 'teresa.rivas@gmail.com', '1987-04-30'),
('Marta Silva', 'marta.silva@gmail.com', '1993-06-06'),
('José Cabrera', 'jose.cabrera@gmail.com', '1990-05-05'),
('Natalia Blanco', 'natalia.blanco@gmail.com', '1989-09-11'),
('David Gil', 'david.gil@gmail.com', '1992-12-19'),
('Sandra Iglesias', 'sandra.iglesias@gmail.com', '1994-08-07'),
('Óscar Molina', 'oscar.molina@gmail.com', '1991-03-01'),
('Andrea Santos', 'andrea.santos@gmail.com', '1997-10-15'),
('Vicente Varela', 'vicente.varela@gmail.com', '1983-12-24'),
('Elisa Suárez', 'elisa.suarez@gmail.com', '1996-06-30'),
('Ignacio Torres', 'ignacio.torres@gmail.com', '1988-05-02'),
('Paula Domínguez', 'paula.dominguez@gmail.com', '1999-02-28'),
('Miguel Ríos', 'miguel.rios@gmail.com', '1993-07-19'),
('Lucía Prieto', 'lucia.prieto@gmail.com', '2000-10-10');

-- ============================================================
--  TRATAMIENTOS (40)
-- ============================================================

INSERT INTO tratamientos (id_tratamiento, nombre, descripcion) VALUES
(1001, 'Fisioterapia', 'Rehabilitación muscular y articular'),
(1002, 'Rehabilitación avanzada', 'Terapia integral postoperatoria'),

(1003, 'Ortodoncia', 'Corrección dental'),
(1004, 'Ortodoncia estética', 'Alineación dental estética'),

(1005, 'Terapia psicológica', 'Apoyo emocional y conductual'),
(1006, 'Psicoterapia infantil', 'Tratamiento psicológico infantil'),

(1007, 'Cardiología preventiva', 'Control cardiovascular y diagnóstico precoz'),
(1008, 'Cardiología intervencionista', 'Cateterismo y tratamiento de arritmias'),

(1009, 'Dermatología estética', 'Tratamientos faciales y de la piel'),
(1010, 'Dermatología clínica', 'Tratamientos de enfermedades cutáneas'),

(1011, 'Nutrición clínica', 'Planes alimentarios personalizados'),
(1012, 'Nutrición deportiva', 'Optimización del rendimiento físico'),

(1013, 'Radioterapia', 'Tratamiento oncológico'),
(1014, 'Oncología pediátrica', 'Tratamiento del cáncer infantil'),

(1015, 'Oftalmología', 'Cirugía ocular y revisión visual'),
(1016, 'Cirugía oftalmológica', 'Corrección visual avanzada'),

(1017, 'Fonoaudiología', 'Tratamiento del habla y la voz'),
(1018, 'Logopedia avanzada', 'Tratamiento de lenguaje y deglución'),

(1019, 'Neumología', 'Tratamiento respiratorio'),
(1020, 'Neumología avanzada', 'Revisión pulmonar y rehabilitación respiratoria'),

(1021, 'Endocrinología', 'Control hormonal y metabólico'),
(1022, 'Endocrinología infantil', 'Control endocrino pediátrico'),

(1023, 'Reumatología', 'Tratamiento de enfermedades articulares'),
(1024, 'Reumatología avanzada', 'Terapia articular especializada'),

(1025, 'Gastroenterología', 'Tratamiento digestivo y hepático'),
(1026, 'Gastroenterología pediátrica', 'Trastornos digestivos infantiles'),

(1027, 'Pediatría', 'Atención médica infantil'),
(1028, 'Pediatría general', 'Revisiones y control del crecimiento'),

(1029, 'Geriatría', 'Atención médica a mayores'),
(1030, 'Geriatría avanzada', 'Atención multidisciplinar al anciano'),

(1031, 'Traumatología', 'Lesiones óseas y musculares'),
(1032, 'Traumatología deportiva', 'Lesiones deportivas'),

(1033, 'Anestesiología', 'Control del dolor y sedación'),
(1034, 'Anestesia general', 'Procedimientos quirúrgicos mayores'),

(1035, 'Urología', 'Tracto urinario y salud renal'),
(1036, 'Urología avanzada', 'Cirugía de riñón y vejiga'),

(1037, 'Otorrinolaringología', 'Oídos, nariz y garganta'),
(1038, 'Otorrinolaringología avanzada', 'Revisión auditiva completa'),

(1039, 'Medicina interna', 'Diagnóstico y control de enfermedades crónicas'),
(1040, 'Medicina interna avanzada', 'Diagnóstico integral de enfermedades complejas');


-- ============================================================
--  CITAS (400 citas aleatorias)
-- ============================================================

DELETE FROM citas;

INSERT INTO citas (id_paciente, fecha)
SELECT FLOOR(1 + RAND() * 40) AS id_paciente,
       DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND() * 400) DAY) AS fecha
FROM (
  WITH RECURSIVE numeros AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numeros WHERE n < 400
  )
  SELECT * FROM numeros
) AS numeros;


-- ============================================================
--  PACIENTES_TRATAMIENTOS (aleatorio 2–12 pacientes por tratamiento)
-- ============================================================

DELETE FROM pacientes_tratamientos;

INSERT INTO pacientes_tratamientos (id_paciente, id_tratamiento, fecha_inicio)
WITH RECURSIVE tr AS (
  SELECT 1001 AS id_tratamiento
  UNION ALL
  SELECT id_tratamiento + 1 FROM tr WHERE id_tratamiento < 1040
),
k_por_trat AS (
  SELECT id_tratamiento, 2 + FLOOR(RAND() * 11) AS k FROM tr
),
baraja AS (
  SELECT tr.id_tratamiento, p.id_paciente,
         ROW_NUMBER() OVER (PARTITION BY tr.id_tratamiento ORDER BY RAND()) AS rn
  FROM tr CROSS JOIN pacientes p
)
SELECT b.id_paciente, b.id_tratamiento,
       DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND() * 700) DAY)
FROM baraja b
JOIN k_por_trat k USING (id_tratamiento)
WHERE b.rn <= k.k;

-- ============================================================
--  RESUMEN DE RELACIONES
-- ============================================================
-- pacientes (1) --- (N) citas
-- pacientes (N) --- (N) tratamientos (a través de pacientes_tratamientos)
-- tratamientos compartidos con PostgreSQL mediante el campo id_tratamiento
-- ============================================================