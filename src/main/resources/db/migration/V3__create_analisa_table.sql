-- Drop table if it exists
DROP TABLE IF EXISTS analisa;

-- Step 3: Create analisa table with correct id_manrisk type
CREATE TABLE analisa (
                         id BIGSERIAL PRIMARY KEY,
                         nama_risiko VARCHAR(255) NOT NULL,
                         penyebab VARCHAR(255) NOT NULL,
                         akibat VARCHAR(255) NOT NULL,
                         skala_dampak INTEGER,
                         skala_kemungkinan INTEGER,
                         tingkat_risiko VARCHAR(255) NOT NULL,
                         status VARCHAR(50),
                         keterangan VARCHAR(100),
                         id_manrisk VARCHAR(100) UNIQUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP,
                         CONSTRAINT fk_analisa_manrisk FOREIGN KEY (id_manrisk) REFERENCES manrisk(id_manrisk)
);

-- Recommended indexes for performance
CREATE INDEX idx_analisa_status ON analisa(status);
CREATE INDEX idx_analisa_tingkat_risiko ON analisa(tingkat_risiko);
CREATE INDEX idx_analisa_skala_dampak ON analisa(skala_dampak);
CREATE INDEX idx_analisa_skala_kemungkinan ON analisa(skala_kemungkinan);
CREATE INDEX idx_analisa_status_risiko ON analisa(status, tingkat_risiko);
