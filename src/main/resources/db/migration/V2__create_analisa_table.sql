
-- Step 3: Create analisa table with one-to-one relation to manrisk
CREATE TABLE IF NOT EXISTS analisa (
                                       id BIGSERIAL PRIMARY KEY,
                                       id_rencana_kinerja VARCHAR(100) NOT NULL UNIQUE,
                                       nama_risiko VARCHAR(255) NOT NULL,
    penyebab VARCHAR(255) NOT NULL,
    akibat VARCHAR(255) NOT NULL,
    skala_dampak INTEGER,
    skala_kemungkinan INTEGER,
                                       tingkat_risiko INTEGER,
                                       level_risiko VARCHAR(255),
    status VARCHAR(50),
    keterangan VARCHAR(100),
                                       pembuat JSONB,
                                       verifikator JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
    );