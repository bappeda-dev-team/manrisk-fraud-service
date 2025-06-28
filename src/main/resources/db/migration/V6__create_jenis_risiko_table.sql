CREATE TABLE IF NOT EXISTS risiko_kecurangan (
    id BIGSERIAL PRIMARY KEY,
    jenis_risiko VARCHAR(255) NOT NULL,
    uraian TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
)