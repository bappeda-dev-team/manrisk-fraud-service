package cc.kertaskerja.manrisk_fraud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Wajib memasukkan ID Rencana Kinerja dan tidak boleh kosong!")
    private String id_rencana_kinerja;

    private Integer id_pohon;
    private String nama_pohon;
    private Integer level_pohon;
    private String nama_rencana_kinerja;
    private String tahun;
    private String status_rencana_kinerja;
    private String pegawai_id;
    private String nama_pegawai;
    private OperasionalDaerah operasional_daerah;

    @NotBlank(message = "Nama risiko tidak boleh kosong!")
    private String nama_risiko;

    @NotBlank(message = "Jenis risiko tidak boleh kosong")
    private String jenis_risiko;

    @NotBlank(message = "Kemungkinan kecurangan tidak boleh kosong")
    private String kemungkinan_kecurangan;

    @NotBlank(message = "Wajib mengisi kolom indikasi")
    private String indikasi;

    @NotBlank(message = "Wajib mengisi kolom kemungkinan terkait")
    private String kemungkinan_pihak_terkait;

    private String status;
    private String keterangan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated_at;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperasionalDaerah {
        private String kode_opd;
        private String nama_opd;
    }
}
