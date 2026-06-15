CREATE TABLE IF NOT EXISTS tujuan_ex (
                                             id BIGSERIAL PRIMARY KEY,
    kode_tujuan_pemda VARCHAR(255),
                                             kode_tujuan_opd VARCHAR(255),
                                             tahun INTEGER,
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMP
);

-- 1. Add relation to identifikasi
ALTER TABLE identifikasi
    ADD COLUMN id_tujuan_ex BIGINT,
    ADD CONSTRAINT fk_identifikasi_tujuan
        FOREIGN KEY (id_tujuan_ex) REFERENCES tujuan_ex(id) ON DELETE CASCADE;

-- 2. Add relation to analisa
ALTER TABLE analisa
    ADD COLUMN id_tujuan_ex BIGINT,
    ADD CONSTRAINT fk_analisa_tujuan
        FOREIGN KEY (id_tujuan_ex) REFERENCES tujuan_ex(id) ON DELETE CASCADE;

-- 3. Add relation to penanganan
ALTER TABLE penanganan
    ADD COLUMN id_tujuan_ex BIGINT,
    ADD CONSTRAINT fk_penanganan_tujuan
        FOREIGN KEY (id_tujuan_ex) REFERENCES tujuan_ex(id) ON DELETE CASCADE;

-- 4. Add relation to pemantauan
ALTER TABLE pemantauan
    ADD COLUMN id_tujuan_ex BIGINT,
    ADD CONSTRAINT fk_pemantauan_tujuan
        FOREIGN KEY (id_tujuan_ex) REFERENCES tujuan_ex(id) ON DELETE CASCADE;

-- 5. Add relation to hasil_pemantauan
ALTER TABLE hasil_pemantauan
    ADD COLUMN id_tujuan_ex BIGINT,
    ADD CONSTRAINT fk_hasil_pemantauan_tujuan
        FOREIGN KEY (id_tujuan_ex) REFERENCES tujuan_ex(id) ON DELETE CASCADE;