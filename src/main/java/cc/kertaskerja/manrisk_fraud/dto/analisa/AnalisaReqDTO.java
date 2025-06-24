package cc.kertaskerja.manrisk_fraud.dto.analisa;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalisaReqDTO {

    @NotBlank(message = "Wajib memasukkan ID Rencana Kinerja dan tidak boleh kosong!")
    @JsonProperty("id_rencana_kinerja")
    private String id_rencana_kinerja;

    @NotBlank(message = "Nama risiko tidak boleh kosong!")
    @JsonProperty("nama_risiko")
    private String nama_risiko;

    @NotBlank(message = "Kolom penyebab tidak boleh kosong!")
    @JsonProperty("penyebab")
    private String penyebab;

    @NotBlank(message = "Kolom akibat tidak boleh kosong!")
    @JsonProperty("akibat")
    private String akibat;

    @NotNull(message = "Skala dampak tidak boleh kosong!")
    @Min(value = 1, message = "Skala dampak minimal 1")
    @Max(value = 5, message = "Skala dampak maksimal 5")
    @JsonProperty("skala_dampak")
    private Integer skala_dampak;

    @NotNull(message = "Skala kemungkinan tidak boleh kosong!")
    @Min(value = 1, message = "Skala kemungkinan minimal 1")
    @Max(value = 5, message = "Skala kemungkinan maksimal 5")
    @JsonProperty("skala_kemungkinan")
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

    @Override
    public String toString() {
        return "AnalisaReqDTO{" +
                "id_rencana_kinerja='" + id_rencana_kinerja + '\'' +
                ", nama_risiko='" + nama_risiko + '\'' +
                ", penyebab='" + penyebab + '\'' +
                ", akibat='" + akibat + '\'' +
                ", skala_dampak=" + skala_dampak +
                ", skala_kemungkinan=" + skala_kemungkinan +
                ", pembuat=" + pembuat +
                '}';
    }
}