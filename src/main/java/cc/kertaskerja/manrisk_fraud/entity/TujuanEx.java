package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tujuan_ex")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class TujuanEx extends BaseAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kode_tujuan_pemda")
    private String kodeTujuanPemda;

    @Column(name = "kode_tujuan_opd")
    private String kodeTujuanOpd;

    @Column(name = "tahun")
    private Integer tahun;

    @OneToMany(mappedBy = "tujuan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Identifikasi> identifikasi;

    @OneToMany(mappedBy = "tujuan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Analisa> analisa;

    @OneToMany(mappedBy = "tujuan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Penangangan> penanganan;

    @OneToMany(mappedBy = "tujuan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Pemantauan> pemantauan;

    @OneToMany(mappedBy = "tujuan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<HasilPemantauan> hasilPemantauan;
}