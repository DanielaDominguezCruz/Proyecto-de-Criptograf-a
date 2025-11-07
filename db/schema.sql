CREATE DATABASE IF NOT EXISTS simulador_ataques
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE simulador_ataques;

CREATE TABLE IF NOT EXISTS simulacion (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tipo VARCHAR(30) NOT NULL,
  objetivo_hash VARCHAR(128) NOT NULL,
  parametros TEXT NOT NULL,
  inicio DATETIME NOT NULL,
  fin DATETIME NULL,
  exito TINYINT(1) NOT NULL DEFAULT 0,
  clave_hallada VARCHAR(255) NULL,
  intentos_totales BIGINT NOT NULL DEFAULT 0,
  intentos_por_segundo DOUBLE NOT NULL DEFAULT 0
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS intento_muestral (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_simulacion BIGINT NOT NULL,
  indice BIGINT NOT NULL,
  valor_propuesto VARCHAR(255) NOT NULL,
  timestamp DATETIME NOT NULL,
  CONSTRAINT fk_intento_simulacion FOREIGN KEY (id_simulacion)
    REFERENCES simulacion(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_simulacion_tipo ON simulacion(tipo);
CREATE INDEX idx_intento_simulacion ON intento_muestral(id_simulacion);
