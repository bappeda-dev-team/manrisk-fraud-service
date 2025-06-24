package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.helper.PegawaiInfoConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "analisa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Analisa extends BaseAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_rencana_kinerja")
    private String idRencanaKinerja;

    @Column(name = "nama_risiko")
    private String namaRisiko;

    @Column(name = "penyebab")
    private String penyebab;

    @Column(name = "akibat")
    private String akibat;

    @Column(name = "skala_dampak")
    private int skalaDampak;

    @Column(name = "skala_kemungkinan")
    private int skalaKemungkinan;

    @Column(name = "tingkat_risiko")
    private int tingkatRisiko;

    @Column(name = "level_risiko")
    private String levelRisiko;

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