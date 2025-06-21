package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "pemantauan")
public class Pemantauan extends BaseAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_rekin")
    private String idRekin;

    @Column(name = "pemilik_risiko")
    private String pemilikRisiko;

    @Column(name = "risiko_kecurangan")
    private String risikoKecurangan;

    @Column(name = "deskripsi_kegiatan_pengendalian")
    private String deskripsiKegiatanPengendalian;

    @Column(name = "pic")
    private String pic;

    @Column(name = "rencana_waktu_pelaksanaan")
    private String rencanaWaktuPelaksanaan;

    @Column(name = "realisasi_waktu_pelaksanaan")
    private String realisasiWaktuPelaksanaan;

    @Column(name = "progres_tindak_lanjut")
    private String progresTindakLanjut;

    @Column(name = "bukti_pelaksanaan_tindak_lanjut")
    private String buktiPelaksanaanTidakLanjut;

    @Column(name = "kendala")
    private String kendala;

    @Column(name = "catatan")
    private String catatan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "keterangan")
    private String keterangan;
}
