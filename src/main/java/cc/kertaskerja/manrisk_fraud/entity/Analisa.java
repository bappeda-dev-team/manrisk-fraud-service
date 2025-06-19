package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @Column(name = "status")
    private String status;

    @Column(name = "keterangan")
    private String keterangan;
}
