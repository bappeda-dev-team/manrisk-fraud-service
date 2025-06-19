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
public class PenangananDTO {
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

    // ✅ Nested DTO for partial update
    @Getter
    @Setter
    public static class UpdateStatusDTO {
        @NotBlank(message = "Status tidak boleh kosong")
        private String status;

        private String keterangan;
    }
}
