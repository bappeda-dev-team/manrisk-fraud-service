package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.helper.PegawaiInfoConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "identifikasi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class Identifikasi extends BaseAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_rencana_kinerja")
    private String idRencanaKinerja;

    @Column(name = "nama_risiko")
    private String namaRisiko;

    @Column(name = "jenis_risiko")
    private String jenisRisiko;

    @Column(name = "kemungkinan_kecurangan")
    private String kemungkinanKecurangan;

    @Column(name = "indikasi")
    private String indikasi;

    @Column(name = "kemungkinan_pihak_terkait")
    private String kemungkinanPihakTerkait;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
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
}
