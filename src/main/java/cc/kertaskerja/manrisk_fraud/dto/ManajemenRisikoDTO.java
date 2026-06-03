package cc.kertaskerja.manrisk_fraud.dto;

import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiResDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManajemenRisikoDTO {
    private Long id;
    private String id_rencana_kinerja;

    private Integer id_pohon;
    private String nama_pohon;
    private Integer level_pohon;
    private String nama_rencana_kinerja; //=> Tahapan Proses Bisnis
    private String tahun;
    private String status_rencana_kinerja;
    private String pegawai_id;
    private String nama_pegawai;
    private OperasionalDaerah operasional_daerah;

    // IDENTIFIKASI
    private String nama_risiko;
    private String jenis_risiko;
    private String uraian;
    private String kemungkinan_kecurangan;
    private String indikasi;
    private String kemungkinan_pihak_terkait;

    // ANALISA
    private String penyebab;
    private String akibat;
    private Integer skala_dampak_sebelum_pengendalian;
    private Integer skala_kemungkinan_sebelum_pengendalian;
    private Integer tingkat_risiko_sebelum_pengendalian;
    private String level_risiko_sebelum_pengendalian;

    // PENANGANAN / PENGENDALIAN
    private String existing_control;
    private String jenis_perlakuan_risiko;
    private String rencana_perlakuan_risiko;
    private String biaya_perlakuan_risiko;
    private String target_waktu;
    private String pic;

    // PEMANTAUAN
    private String risiko_kecurangan;
    private String deskripsi_kegiatan_pengendalian;
    private String rencana_waktu_pelaksanaan;
    private String realisasi_waktu_pelaksanaan;
    private String progres_tindak_lanjut;
    private String bukti_pelaksanaan_tindak_lanjut;
    private String kendala;

    // HASIL PEMANTAUAN
    private Integer skala_dampak_setelah_pengendalian;
    private Integer skala_kemungkinan_setelah_pengendalian;
    private Integer tingkat_risiko_setelah_pengendalian;
    private String level_risiko_setelah_pengendalian;
    private String status_hasil_pemantauan;
    private String keterangan_hasil_pemantauan;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperasionalDaerah {
        private String kode_opd;
        private String nama_opd;
    }
}
