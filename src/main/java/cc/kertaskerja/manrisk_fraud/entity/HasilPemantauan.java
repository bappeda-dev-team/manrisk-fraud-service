package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
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
    @Column(name = "id_rekin")
    private String idRekin;

    @Column(name = "skala_dampak")
    private int skalaDampak;

    @Column(name = "skala_kemungkinan")
    private int skalaKemungkinan;

    @Column(name = "tingkat_risiko")
    private Integer tingkatRisiko;

    @Column(name = "level_risiko")
    private String levelRisiko;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_rekin", referencedColumnName = "id_rekin")
    private Pemantauan pemantauan;
}

