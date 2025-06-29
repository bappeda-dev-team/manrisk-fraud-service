-- Step 2: Create identifikasi table
CREATE TABLE IF NOT EXISTS identifikasi (
                                            id BIGSERIAL PRIMARY KEY,
                                            id_rencana_kinerja VARCHAR(100) NOT NULL UNIQUE,
                                            nama_risiko VARCHAR(255) NOT NULL,
    jenis_risiko VARCHAR(255) NOT NULL,
    uraian VARCHAR(255) NOT NULL,
    kemungkinan_kecurangan VARCHAR(255) NOT NULL,
    indikasi VARCHAR(255) NOT NULL,
    kemungkinan_pihak_terkait VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    keterangan VARCHAR(100),
                                            pembuat JSONB,
                                            verifikator JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);