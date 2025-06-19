package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "id_rekin")
    private String idRekin;

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
    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "keterangan")
    private String keterangan;
}
