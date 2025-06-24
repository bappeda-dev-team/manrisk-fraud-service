package cc.kertaskerja.manrisk_fraud.dto.pemantauan;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PemantauanReqDTO {
    @NotBlank(message = "Wajib memasukkan ID Rencana Kinerja dan tidak boleh kosong!")
    private String id_rencana_kinerja;

    @NotBlank(message = "Harap mengisi pemilik risiko")
    private String pemilik_risiko;

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

    @NotBlank(message = "Bukti pelaksanaan tidak boleh kosong!")
    private String bukti_pelaksanaan_tindak_lanjut;

    @NotBlank(message = "Harap isi kendala!")
    private String kendala;

    @NotBlank(message = "Harap isi catatan!")
    private String catatan;

    @Valid
    @NotNull(message = "Pegawai tidak boleh kosong!")
    @JsonProperty("pembuat")
    private PegawaiInfo pembuat;

    @Getter
    @Setter
    public static class UpdateStatusDTO {
        @NotBlank(message = "Status tidak boleh kosong")
        private String status;

        private String keterangan;

        @Valid
        @NotNull(message = "Nama pegawai verifikator tidak boleh kosong")
        @JsonProperty("verifikator")
        private PegawaiInfo verifikator;
    }
}
