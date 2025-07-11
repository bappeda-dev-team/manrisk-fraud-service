package cc.kertaskerja.manrisk_fraud.dto.penanganan;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Data NIP Pegawai dibutuhkan")
    private String nip_pembuat;

    @Getter
    @Setter
    public static class UpdateStatusDTO {
        @NotBlank(message = "Status tidak boleh kosong")
        private String status;

        private String keterangan;

        @NotBlank(message = "NIP pegawai verifikator tidak boleh kosong")
        private String nip_verifikator;
    }
}
