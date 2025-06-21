CREATE TABLE IF NOT EXISTS hasil_pemantauan (
    id BIGSERIAL PRIMARY KEY,
    id_rekin VARCHAR(100) NOT NULL UNIQUE,
    pemilik_risiko VARCHAR(100) NOT NULL,
    risiko_kecurangan VARCHAR(255) NOT NULL,
)