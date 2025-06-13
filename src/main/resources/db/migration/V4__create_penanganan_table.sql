-- Step 4: Create penanganan table
CREATE TABLE IF NOT EXISTS penanganan (
    id BIGSERIAL PRIMARY KEY,
    existing_control VARCHAR(255) NOT NULL,
    jenis_perlakuan_risiko VARCHAR(255) NOT NULL,
    rencana_perlakuan_risiko VARCHAR(255) NOT NULL,
    biaya_perlakuan_risiko VARCHAR(255) NOT NULL,
    target_waktu VARCHAR(255) NOT NULL,
    pic VARCHAR(100),
    status VARCHAR(50),
    keterangan VARCHAR(255),
    id_manrisk VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_penanganan_manrisk FOREIGN KEY (id_manrisk) REFERENCES manrisk(id_manrisk)
    );

-- Indexes for performance
CREATE INDEX idx_penanganan_status ON penanganan(status);
CREATE INDEX idx_penanganan_pic ON penanganan(pic);
CREATE INDEX idx_penanganan_jenis_perlakuan ON penanganan(jenis_perlakuan_risiko);
CREATE INDEX idx_penanganan_target_waktu ON penanganan(target_waktu);
