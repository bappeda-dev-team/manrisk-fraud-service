package cc.kertaskerja.manrisk_fraud.dto.analisa;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalisaResDTO {
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
    private String nama_risiko;
    private String penyebab;
    private String akibat;
    private Integer skala_dampak;
    private Integer skala_kemungkinan;
    private Integer tingkat_risiko;
    private String level_risiko;
    private String status;
    private String keterangan;
    private PegawaiInfo pembuat;
    private PegawaiInfo verifikator;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OperasionalDaerah {
        private String kode_opd;
        private String nama_opd;
    }
}

