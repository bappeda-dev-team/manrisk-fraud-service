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
@EqualsAndHashCode(callSuper = true, exclude = "idManrisk")
@Builder
public class Identifikasi extends BaseAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Nama risiko wajib diisi!")
    @Column(name = "nama_risiko")
    private String namaRisiko;

    @NotNull(message = "Jenis risiko wajib diisi!")
    @Column(name = "jenis_risiko")
    private String jenisRisiko;

    @NotNull(message = "Kemungkinan kecurangan tidak boleh kosong!")
    @Column(name = "kemungkinan_kecurangan")
    private String kemungkinanKecurangan;

    @NotNull(message = "Indikasi wajib diisi!")
    @Column(name = "indikasi")
    private String indikasi;

    @NotNull(message = "Wajib mengisi pihak terkait!")
    @Column(name = "kemungkinan_pihak_terkait")
    private String kemungkinanPihakTerkait;

    @Column(name = "keterangan")
    private String keterangan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_manrisk", unique = true, nullable = false)
    private Manrisk idManrisk;

}
