package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.helper.PegawaiInfoConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hasil_pemantauan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
public class HasilPemantauan extends BaseAuditable {
    @Id
    @Column(name = "id_rencana_kinerja")
    private String idRencanaKinerja;

    @Column(name = "skala_dampak")
    private int skalaDampak;

    @Column(name = "skala_kemungkinan")
    private int skalaKemungkinan;

    @Column(name = "tingkat_risiko")
    private Integer tingkatRisiko;

    @Column(name = "level_risiko")
    private String levelRisiko;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private StatusEnum status;

    @Column(name = "keterangan")
    private String keterangan;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_rencana_kinerja", referencedColumnName = "id_rencana_kinerja")
    @JsonBackReference
    private Pemantauan pemantauan;

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

