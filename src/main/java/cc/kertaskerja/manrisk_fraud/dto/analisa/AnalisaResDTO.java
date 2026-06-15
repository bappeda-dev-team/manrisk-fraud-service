package cc.kertaskerja.manrisk_fraud.dto.analisa;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalisaResDTO {
    private Long id;

    private TujuanOpdDTO tujuan_opd;
    private RencanaKinerjaDTO rencana_kinerja;

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
    public static class RencanaKinerjaDTO {
        private String id_rencana_kinerja;
        private Integer id_pohon;
        private String nama_pohon;
        private Integer level_pohon;
        private String nama_rencana_kinerja;
        private String tahun;
        private String status_rencana_kinerja;
        private String pegawai_id;
        private String nama_pegawai;
        private String kode_opd;
        private String nama_opd;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TujuanOpdDTO {
        private String kode_tujuan_opd;
        private String tujuan_opd;
        private String periode;
        private List<IndikatorDTO> indikators;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IndikatorDTO {
        private Long id;
        private String kode_indikator;
        private String indikator;
        private String rumus_perhitungan;
        private String sumber_data;
        private String definisi_operasional;
        private Integer tahun_aktif;
        private List<TargetDTO> targets;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TargetDTO {
        private Long id;
        private String kode_target;
        private Integer tahun;
        private Integer target;
        private String satuan;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AnalisaByTujuanResDTO {

        private String kode_tujuan_opd;
        private String tujuan_opd;
        private String periode;

        private List<AnalisaResDTO.IndikatorDTO> indikators;

        private List<AnalisaResDTO> analisa;
    }
}