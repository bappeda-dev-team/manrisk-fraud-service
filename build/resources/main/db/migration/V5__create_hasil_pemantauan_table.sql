CREATE TABLE IF NOT EXISTS hasil_pemantauan (
                                                id BIGSERIAL,
                                                id_rencana_kinerja VARCHAR(100) PRIMARY KEY,
                                                skala_dampak INTEGER NOT NULL,
                                                skala_kemungkinan INTEGER NOT NULL,
                                                tingkat_risiko INTEGER,
                                                level_risiko VARCHAR(50),
                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                updated_at TIMESTAMP,
                                                pembuat JSONB,
                                                verifikator JSONB,
                                                CONSTRAINT fk_hasil_pemantauan_id_rencana_kinerja
                                                    FOREIGN KEY (id_rencana_kinerja)
                                                        REFERENCES pemantauan(id_rencana_kinerja)
                                                        ON DELETE CASCADE
);