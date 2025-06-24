package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.helper.PegawaiInfoConverter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pemantauan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Pemantauan extends BaseAuditable {
    @Id
    @Column(name = "id_rencana_kinerja", nullable = false, unique = true)
    private String idRencanaKinerja;

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
    private String buktiPelaksanaanTindakLanjut;

    @Column(name = "kendala")
    private String kendala;

    @Column(name = "catatan")
    private String catatan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "keterangan")
    private String keterangan;

    @Convert(converter = PegawaiInfoConverter.class)
    @Column(name = "pembuat", columnDefinition = "jsonb")
    @org.hibernate.annotations.ColumnTransformer(
            read = "pembuat::text",
            write = "?::jsonb"
    )
    private PegawaiInfo pembuat;

    @Convert(converter = PegawaiInfoConverter.class)
    @Column(name = "verifikator", columnDefinition = "jsonb")
    @org.hibernate.annotations.ColumnTransformer(
            read = "verifikator::text",
            write = "?::jsonb"
    )
    private PegawaiInfo verifikator;

    @OneToOne(mappedBy = "pemantauan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonManagedReference
    private HasilPemantauan hasilPemantauan;
}
