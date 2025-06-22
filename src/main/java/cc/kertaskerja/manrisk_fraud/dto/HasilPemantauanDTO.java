package cc.kertaskerja.manrisk_fraud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class HasilPemantauanDTO {
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

    private String pemilik_risiko;
    private String risiko_kecurangan;
    private String deskripsi_kegiatan_pengendalian;
    private String pic;
    private String rencana_waktu_pelaksanaan;
    private String realisasi_waktu_pelaksanaan;
    private String progres_tindak_lanjut;
    private String bukti_pelaksanaan_tindak_lanjut;
    private String kendala;
    private String catatan;
    private String status;
    private String keterangan;

    @NotNull(message = "Skala dampak tidak boleh kosong!")
    private Integer skala_dampak;

    @NotNull(message = "Skala kemungkinan tidak boleh kosong!")
    private Integer skala_kemungkinan;

    private Integer tingkat_risiko;
    private String level_risiko;

    private PemantauanDTO pemantauanDTO;

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

