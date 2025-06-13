package cc.kertaskerja.manrisk_fraud.dto.analisa;

import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private Integer tingkat_risiko;
    private String level_risiko;
    private String status;
    private String keterangan;

    private Integer id_pohon;
    private String nama_pohon;
    private String nama_rencana_kinerja;
    private String tahun;
    private String status_rencana_kinerja;
    private AnalisaDTO.OperasionalDaerah operasional_daerah;
    private String id_pegawai;
    private String nama_pegawai;

    @NotNull(message = "idManrisk wajib diisi")
    @JsonProperty("id_manrisk")
    private String id_manrisk;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperasionalDaerah {
        private String kode_opd;
        private String nama_opd;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private java.time.LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private java.time.LocalDateTime updatedAt;
}
