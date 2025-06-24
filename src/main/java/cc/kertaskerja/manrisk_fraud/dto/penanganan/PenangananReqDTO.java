package cc.kertaskerja.manrisk_fraud.dto.penanganan;

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
public class PenangananReqDTO {
    @NotBlank(message = "Wajib memasukkan ID Rencana Kinerja dan tidak boleh kosong!")
    private String id_rencana_kinerja;

    @NotBlank(message = "Existing Control wajib diisi!")
    private String existing_control;

    @NotBlank(message = "Jenis perlakuan risiko tidak boleh koson!")
    private String jenis_perlakuan_risiko;

    @NotBlank(message = "Rencana perlakuan risiko tidak boleh kosong!")
    private String rencana_perlakuan_risiko;

    @NotBlank(message = "Biaya perlakuan risiko tidak boleh kosong!")
    private String biaya_perlakuan_risiko;

    @NotBlank(message = "Harap mengisi target waktu")
    private String target_waktu;

    @NotBlank(message = "PIC wajib diisi!")
    private String pic;

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
