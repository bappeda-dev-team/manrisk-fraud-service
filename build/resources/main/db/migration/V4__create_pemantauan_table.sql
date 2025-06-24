DROP TABLE IF EXISTS pemantauan;

CREATE TABLE pemantauan (
                            id BIGSERIAL,
                            id_rencana_kinerja VARCHAR(100) PRIMARY KEY,
                            pemilik_risiko VARCHAR(100) NOT NULL,
                            risiko_kecurangan VARCHAR(255) NOT NULL,
                            deskripsi_kegiatan_pengendalian VARCHAR(255) NOT NULL,
                            pic VARCHAR(255) NOT NULL,
                            rencana_waktu_pelaksanaan VARCHAR(255) NOT NULL,
                            realisasi_waktu_pelaksanaan VARCHAR(255) NOT NULL,
                            progres_tindak_lanjut VARCHAR(255) NOT NULL,
                            bukti_pelaksanaan_tindak_lanjut VARCHAR(255) NOT NULL,
                            kendala VARCHAR(255) NOT NULL,
                            catatan VARCHAR(255),
                            status VARCHAR(50),
                            keterangan VARCHAR(100),
                            pembuat JSONB,
                            verifikator JSONB,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP
);
