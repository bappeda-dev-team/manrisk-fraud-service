package cc.kertaskerja.manrisk_fraud.dto.identifikasi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdentifikasiDTO {
    private Long id;
    @JsonProperty("nama_risiko")
    private String nama_risiko;

    @JsonProperty("jenis_risiko")
    private String jenis_risiko;

    @JsonProperty("kemungkinan_kecurangan")
    private String kemungkinan_kecurangan;

    @JsonProperty("indikasi")
    private String indikasi;

    @JsonProperty("kemungkinan_pihak_terkait")
    private String kemungkinan_pihak_terkait;

    @JsonProperty("keterangan")
    private String keterangan;


    private Integer id_pohon;
    private String nama_pohon;
    private String nama_rencana_kinerja;
    private String tahun;
    private String status_rencana_kinerja;
    private OperasionalDaerah operasional_daerah;
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
