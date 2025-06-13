-- Step 2: Create identifikasi table
CREATE TABLE IF NOT EXISTS identifikasi (
                                            id BIGSERIAL PRIMARY KEY,
                                            nama_risiko VARCHAR(255) NOT NULL,
    jenis_risiko VARCHAR(255) NOT NULL,
    kemungkinan_kecurangan VARCHAR(255) NOT NULL,
    indikasi VARCHAR(255) NOT NULL,
    kemungkinan_pihak_terkait VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    keterangan VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    id_manrisk VARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (id_manrisk) REFERENCES manrisk(id_manrisk)
    );

CREATE INDEX IF NOT EXISTS idx_identifikasi_nama_risiko ON identifikasi (nama_risiko);
CREATE INDEX IF NOT EXISTS idx_identifikasi_jenis_risiko ON identifikasi (jenis_risiko);
CREATE INDEX IF NOT EXISTS idx_identifikasi_status ON identifikasi (status);
CREATE INDEX IF NOT EXISTS idx_identifikasi_created_at ON identifikasi (created_at);