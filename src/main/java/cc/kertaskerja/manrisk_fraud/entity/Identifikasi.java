package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
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

    @Column(name = "id_rekin")
    private String idRekin;

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

    @Column(name = "status")
    private String status;

    @Column(name = "keterangan")
    private String keterangan;
}
