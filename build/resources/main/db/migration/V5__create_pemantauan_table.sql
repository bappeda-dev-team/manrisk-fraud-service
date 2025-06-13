DROP TABLE IF EXISTS pemantauan;

CREATE TABLE pemantauan (
                            id BIGSERIAL PRIMARY KEY,
                            risiko_kecurangan VARCHAR(255) NOT NULL,
                            deskripsi_kegiatan_pengendalian VARCHAR(255) NOT NULL,
                            rencana_waktu_pelaksanaan VARCHAR(255) NOT NULL,
                            realisasi_waktu_pelaksanaan VARCHAR(255) NOT NULL,
                            progres_tindak_lanjut VARCHAR(255) NOT NULL,
                            skala_dampak INTEGER,
                            skala_kemungkinan INTEGER,
                            tingkat_risiko VARCHAR(255),
                            bukti_pelaksanaan VARCHAR(255) NOT NULL,
                            kendala VARCHAR(255) NOT NULL,
                            catatan VARCHAR(255) NOT NULL,
                            status VARCHAR(50),
                            keterangan VARCHAR(100),
                            id_manrisk VARCHAR(100) UNIQUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP,
                            CONSTRAINT fk_pemantauan_manrisk FOREIGN KEY (id_manrisk) REFERENCES manrisk(id_manrisk)
);

-- Indexes to improve query performance
CREATE INDEX idx_pemantauan_status ON pemantauan(status);
CREATE INDEX idx_pemantauan_tingkat_risiko ON pemantauan(tingkat_risiko);
CREATE INDEX idx_pemantauan_skala_dampak ON pemantauan(skala_dampak);
CREATE INDEX idx_pemantauan_skala_kemungkinan ON pemantauan(skala_kemungkinan);
