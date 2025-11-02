-- ============================================================
--  BASE DE DATOS POSTGRESQL: hospital_postgre
--  Codificación: UTF-8
--  Descripción: Sincronizada con hospital_mysql (tratamientos 1001–1040)
-- ============================================================

-- DROP DATABASE IF EXISTS hospital_postgre;
-- CREATE DATABASE hospital_postgre;
-- \c hospital_postgre;

-- Borrar esquema anterior
DROP SCHEMA IF EXISTS hospital CASCADE;
CREATE SCHEMA hospital;

-- ============================================================
--  TIPOS Y TABLAS
-- ============================================================

CREATE TYPE hospital.contacto_medico AS (
  nombre_contacto VARCHAR(255),
  nif VARCHAR(20),
  telefono INTEGER,
  email VARCHAR(255)
);

CREATE TABLE hospital.medicos (
  id_medico SERIAL PRIMARY KEY,
  nombre_medico VARCHAR(100) NOT NULL,
  contacto hospital.contacto_medico
);

CREATE TABLE hospital.especialidades (
  id_especialidad SERIAL PRIMARY KEY,
  nombre_especialidad VARCHAR(100) NOT NULL
);
-- Crear la secuencia para generar IDs automáticos de tratamientos despues del 1040 ya que hasta ese número estan insertados en el Script

CREATE SEQUENCE hospital.tratamiento_seq START 1041;

CREATE TABLE hospital.tratamientos (
  id_tratamiento INTEGER PRIMARY KEY,
  id_medico INTEGER,
  id_especialidad INTEGER
);

CREATE TABLE hospital.salas (
  id_sala SERIAL PRIMARY KEY,
  nombre_sala VARCHAR(100) NOT NULL,
  ubicacion VARCHAR(100)
);

-- Crear la tabla intermedia entre salas y tratamientos
CREATE TABLE hospital.salas_tratamientos (
  id_sala INTEGER NOT NULL,
  id_tratamiento INTEGER NOT NULL,
  PRIMARY KEY (id_sala, id_tratamiento)
);

-- ============================================================
--  CLAVES FORÁNEAS
-- ============================================================

-- Relación tratamientos → médicos (varios a uno)
ALTER TABLE hospital.tratamientos
ADD CONSTRAINT fk_tratamientos_medicos
FOREIGN KEY (id_medico) REFERENCES hospital.medicos(id_medico)
ON DELETE CASCADE ON UPDATE CASCADE; -- Si se elimina o actualiza un médico, se refleja en tratamientos

-- Relación tratamientos → especialidades (varios a uno)
ALTER TABLE hospital.tratamientos
ADD CONSTRAINT fk_tratamientos_especialidades
FOREIGN KEY (id_especialidad) REFERENCES hospital.especialidades(id_especialidad)
ON DELETE CASCADE ON UPDATE CASCADE;

-- Relación salas_tratamientos → salas (varios a uno)
ALTER TABLE hospital.salas_tratamientos
ADD CONSTRAINT fk_salas_tratamientos_salas
FOREIGN KEY (id_sala) REFERENCES hospital.salas(id_sala)
ON DELETE CASCADE ON UPDATE CASCADE;

-- Relación salas_tratamientos → tratamientos (varios a uno)
ALTER TABLE hospital.salas_tratamientos
ADD CONSTRAINT fk_salas_tratamientos_tratamientos
FOREIGN KEY (id_tratamiento) REFERENCES hospital.tratamientos(id_tratamiento)
ON DELETE CASCADE ON UPDATE CASCADE;

-- ============================================================
--  INSERCIÓN DE DATOS DE EJEMPLO
-- ============================================================

-- ============================================================
--  ESPECIALIDADES (20)
-- ============================================================

INSERT INTO hospital.especialidades (nombre_especialidad) VALUES
('Rehabilitación'),
('Ortodoncia'),
('Psicología'),
('Cardiología'),
('Dermatología'),
('Nutrición'),
('Oncología'),
('Oftalmología'),
('Fonoaudiología'),
('Neumología'),
('Endocrinología'),
('Reumatología'),
('Gastroenterología'),
('Pediatría'),
('Geriatría'),
('Traumatología'),
('Anestesiología'),
('Urología'),
('Otorrinolaringología'),
('Medicina Interna');

-- ============================================================
--  MÉDICOS (20)
-- ============================================================

INSERT INTO hospital.medicos (nombre_medico, contacto) VALUES
('Dr. Javier Muñoz', ROW('Javier Muñoz','12345678A',612345678,'javier@hospital.test')),
('Dra. Laura Nieto', ROW('Laura Nieto','87654321B',698765432,'laura@hospital.test')),
('Dr. Pedro Sánchez', ROW('Pedro Sánchez','22223333C',677889900,'pedro@hospital.test')),
('Dra. Elena López', ROW('Elena López','44445555D',612223334,'elena@hospital.test')),
('Dr. Andrés Ramos', ROW('Andrés Ramos','55556666E',600111222,'andres@hospital.test')),
('Dra. Lucía Torres', ROW('Lucía Torres','66667777F',633222333,'lucia@hospital.test')),
('Dr. Marcos Díaz', ROW('Marcos Díaz','77778888G',655444555,'marcos@hospital.test')),
('Dra. Patricia Vera', ROW('Patricia Vera','88889999H',677555666,'patricia@hospital.test')),
('Dr. Carlos Ruiz', ROW('Carlos Ruiz','99990000I',699666777,'carlos@hospital.test')),
('Dra. Teresa Gil', ROW('Teresa Gil','11112222J',622777888,'teresa@hospital.test')),
('Dr. Julio Martín', ROW('Julio Martín','33334444K',611223344,'julio@hospital.test')),
('Dra. Mónica Herrera', ROW('Mónica Herrera','55556666L',634556677,'monica@hospital.test')),
('Dr. Antonio Vega', ROW('Antonio Vega','77778888M',655667788,'antonio@hospital.test')),
('Dra. Isabel Suárez', ROW('Isabel Suárez','88889999N',677778899,'isabel@hospital.test')),
('Dr. Eduardo Blanco', ROW('Eduardo Blanco','11112222O',622889900,'eduardo@hospital.test')),
('Dra. Carolina Prado', ROW('Carolina Prado','22223333P',600112233,'carolina@hospital.test')),
('Dr. Roberto León', ROW('Roberto León','33334444Q',633223344,'roberto@hospital.test')),
('Dra. Alicia Torres', ROW('Alicia Torres','44445555R',655334455,'alicia@hospital.test')),
('Dr. Miguel Castro', ROW('Miguel Castro','55556666S',677445566,'miguel@hospital.test')),
('Dra. Natalia Gómez', ROW('Natalia Gómez','66667777T',699556677,'natalia@hospital.test'));

-- ============================================================
--  SALAS (10)
-- ============================================================

INSERT INTO hospital.salas (nombre_sala, ubicacion) VALUES
('Sala 1', 'Planta 1'),
('Sala 2', 'Planta 1'),
('Sala 3', 'Planta 2'),
('Sala 4', 'Planta 2'),
('Sala 5', 'Planta 3'),
('Sala 6', 'Planta 3'),
('Sala 7', 'Planta 4'),
('Sala 8', 'Planta 4'),
('Sala 9', 'Planta 5'),
('Sala 10', 'Planta 5');

-- ============================================================
--  TRATAMIENTOS (40 sincronizados con MySQL)
-- ============================================================

INSERT INTO hospital.tratamientos (id_tratamiento, id_medico, id_especialidad)
SELECT id_tratamiento,
       ((id_tratamiento - 1001) % 20) + 1,  -- Rota entre 20 médicos
       ((id_tratamiento - 1001) % 20) + 1   -- Rota entre 20 especialidades
FROM generate_series(1001,1040) AS id_tratamiento;


-- ============================================================
--  SALAS_TRATAMIENTOS (asignación aleatoria 1–3 salas por tratamiento)
-- ============================================================

-- Eliminamos datos previos
DELETE FROM hospital.salas_tratamientos;

DO $$
DECLARE
    t_id INTEGER;
    s_count INTEGER;
    s_id INTEGER;
BEGIN
    -- Para cada tratamiento (1001 a 1040)
    FOR t_id IN 1001..1040 LOOP
        -- Cantidad aleatoria de salas (entre 1 y 3)
        s_count := FLOOR(RANDOM() * 3) + 1;

        -- Insertar ese número aleatorio de relaciones
        FOR i IN 1..s_count LOOP
            s_id := FLOOR(RANDOM() * 10) + 1; -- ID de sala entre 1 y 10
            BEGIN
                INSERT INTO hospital.salas_tratamientos (id_sala, id_tratamiento)
                VALUES (s_id, t_id)
                ON CONFLICT DO NOTHING; -- Evitar duplicados
            EXCEPTION WHEN OTHERS THEN
                -- Ignorar duplicados si ocurren
                NULL;
            END;
        END LOOP;
    END LOOP;
END $$;

-- ============================================================
--  RESUMEN DE RELACIONES
-- ============================================================
-- especialidades (1) --- (N) tratamientos
-- medicos (1) --- (N) tratamientos
-- tratamientos (N) --- (N) salas (a través de salas_tratamientos)
-- Los tratamientos se sincronizan con la BD MySQL mediante el id_tratamiento
-- ============================================================
