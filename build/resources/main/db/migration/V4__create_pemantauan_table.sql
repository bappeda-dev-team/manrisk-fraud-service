DROP TABLE IF EXISTS pemantauan;

CREATE TABLE pemantauan (
                            id BIGSERIAL PRIMARY KEY,
                            id_rekin VARCHAR(100) UNIQUE,
                            risiko_kecurangan VARCHAR(255) NOT NULL,
                            deskripsi_kegiatan_pengendalian VARCHAR(255) NOT NULL,
                            pic VARCHAR(255) NOT NULL,
                            rencana_waktu_pelaksanaan VARCHAR(255) NOT NULL,
                            realisasi_waktu_pelaksanaan VARCHAR(255) NOT NULL,
                            progres_tindak_lanjut VARCHAR(255) NOT NULL,
                            skala_dampak INTEGER,
                            skala_kemungkinan INTEGER,
                            tingkat_risiko INTEGER,
                            level_risiko VARCHAR(255),
                            bukti_pelaksanaan VARCHAR(255) NOT NULL,
                            kendala VARCHAR(255) NOT NULL,
                            catatan VARCHAR(255),
                            status VARCHAR(50),
                            keterangan VARCHAR(100),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP
);
