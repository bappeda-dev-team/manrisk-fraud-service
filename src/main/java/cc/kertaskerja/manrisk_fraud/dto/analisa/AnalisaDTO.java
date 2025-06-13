package cc.kertaskerja.manrisk_fraud.dto.analisa;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisaDTO {
    private Long id;
    private String nama_risiko;
    private String penyebab;
    private String akibat;
    private Integer skala_dampak;
    private Integer skala_kemungkinan;
    private String tingkat_risiko;
    private String status;
    private String keterangan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private java.time.LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private java.time.LocalDateTime updatedAt;
}
