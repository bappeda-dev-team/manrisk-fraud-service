CREATE TABLE IF NOT EXISTS hasil_pemantauan (
                                                id BIGSERIAL,
                                  id_rekin VARCHAR(100) PRIMARY KEY,
                                  skala_dampak INTEGER NOT NULL,
                                  skala_kemungkinan INTEGER NOT NULL,
                                  tingkat_risiko INTEGER,
                                  level_risiko VARCHAR(50),
                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                updated_at TIMESTAMP,
                                  CONSTRAINT fk_hasil_pemantauan_id_rekin FOREIGN KEY (id_rekin) REFERENCES pemantauan(id_rekin) ON DELETE CASCADE
);