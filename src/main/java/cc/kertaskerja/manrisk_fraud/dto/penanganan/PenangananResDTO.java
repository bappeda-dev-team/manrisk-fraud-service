package cc.kertaskerja.manrisk_fraud.dto.penanganan;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.helper.PegawaiInfoConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PenangananResDTO {
    private Long id;
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

    private String existing_control;
    private String jenis_perlakuan_risiko;
    private String rencana_perlakuan_risiko;
    private String biaya_perlakuan_risiko;
    private String target_waktu;
    private String pic;
    private String status;
    private String keterangan;
    private PegawaiInfo pembuat;
    private PegawaiInfo verifikator;

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
