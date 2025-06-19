package cc.kertaskerja.manrisk_fraud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PemantauanDTO {
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

    @NotBlank(message = "Risiko kecurangan tidak boleh kosong!")
    private String risiko_kecurangan;

    @NotBlank(message = "Harap isi deskripsi kegiatan pengendalian")
    private String deskripsi_kegiatan_pengendalian;

    @NotBlank(message = "Harap isi penanggung jawab")
    private String pic;

    @NotBlank(message = "Rencana waktu kegiatan tidak boleh koson!")
    private String rencana_waktu_pelaksanaan;

    @NotBlank(message = "Realisasi waktu pelaksanaan tidak boleh kosong!")
    private String realisasi_waktu_pelaksanaan;

    @NotBlank(message = "Progres tindak lanjut tidak boleh kosong!")
    private String progres_tindak_lanjut;

    @NotNull(message = "Skala dampak tidak boleh kosong!")
    @Min(value = 1, message = "Skala dampak minimal 1")
    @Max(value = 5, message = "Skala dampak maksimal 5")
    private int skala_dampak;

    @NotNull(message = "Skala kemungkinan tidak boleh kosong!")
    @Min(value = 1, message = "Skala kemungkinan minimal 1")
    @Max(value = 5, message = "Skala kemungkinan maksimal 5")
    private int skala_kemungkinan;

    private int tingkat_risiko;

    private String level_risiko;

    @NotBlank(message = "Bukti pelaksanaan tidak boleh kosong!")
    private String bukti_pelaksanaan;

    @NotBlank(message = "Harap isi kendala!")
    private String kendala;

    private String catatan;
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
