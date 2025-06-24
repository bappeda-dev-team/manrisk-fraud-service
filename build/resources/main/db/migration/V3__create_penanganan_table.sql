-- Step 4: Create penanganan table
CREATE TABLE IF NOT EXISTS penanganan (
    id BIGSERIAL PRIMARY KEY,
    id_rencana_kinerja VARCHAR(100) UNIQUE,
    existing_control VARCHAR(255) NOT NULL,
    jenis_perlakuan_risiko VARCHAR(255) NOT NULL,
    rencana_perlakuan_risiko VARCHAR(255) NOT NULL,
    biaya_perlakuan_risiko VARCHAR(255) NOT NULL,
    target_waktu VARCHAR(255) NOT NULL,
    pic VARCHAR(100),
    status VARCHAR(50),
    keterangan VARCHAR(255),
    pembuat JSONB,
    verifikator JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
    );
