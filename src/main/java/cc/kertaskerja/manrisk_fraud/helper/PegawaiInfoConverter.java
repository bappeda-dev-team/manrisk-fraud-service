package cc.kertaskerja.manrisk_fraud.helper;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter(autoApply = false)
public class PegawaiInfoConverter implements AttributeConverter<PegawaiInfo, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(PegawaiInfo pegawaiInfo) {
        if (pegawaiInfo == null) return null;
        try {
            return objectMapper.writeValueAsString(pegawaiInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert PegawaiInfo to JSON", e);
        }
    }

    @Override
    public PegawaiInfo convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) return null;
        try {
            return objectMapper.readValue(dbData, PegawaiInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to PegawaiInfo", e);
        }
    }
}