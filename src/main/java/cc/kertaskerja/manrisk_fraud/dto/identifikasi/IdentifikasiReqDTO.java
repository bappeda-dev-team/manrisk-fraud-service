package cc.kertaskerja.manrisk_fraud.dto.identifikasi;

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
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdentifikasiReqDTO {
    @NotBlank(message = "Wajib memasukkan ID Rencana Kinerja dan tidak boleh kosong!")
    private String id_rencana_kinerja;

    @NotBlank(message = "Nama risiko tidak boleh kosong!")
    private String nama_risiko;

    @NotBlank(message = "Jenis risiko tidak boleh kosong")
    private String jenis_risiko;

    @NotBlank(message = "Wajib mengisi kolom uraian jenis risiko")
    private String uraian;

    @NotBlank(message = "Kemungkinan kecurangan tidak boleh kosong")
    private String kemungkinan_kecurangan;

    @NotBlank(message = "Wajib mengisi kolom indikasi")
    private String indikasi;

    @NotBlank(message = "Wajib mengisi kolom kemungkinan terkait")
    private String kemungkinan_pihak_terkait;

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
