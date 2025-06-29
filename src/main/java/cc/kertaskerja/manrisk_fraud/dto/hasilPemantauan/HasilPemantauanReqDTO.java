package cc.kertaskerja.manrisk_fraud.dto.hasilPemantauan;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.dto.pemantauan.PemantauanResDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class HasilPemantauanReqDTO {
    @NotBlank(message = "Wajib memasukkan ID Rencana Kinerja dan tidak boleh kosong!")
    private String id_rencana_kinerja;

    @NotNull(message = "Skala dampak tidak boleh kosong!")
    @Min(value = 1, message = "Skala dampak minimal 1")
    @Max(value = 5, message = "Skala dampak maksimal 5")
    private Integer skala_dampak;

    @NotNull(message = "Skala kemungkinan tidak boleh kosong!")
    @Min(value = 1, message = "Skala kemungkinan minimal 1")
    @Max(value = 5, message = "Skala kemungkinan maksimal 5")
    private Integer skala_kemungkinan;

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
