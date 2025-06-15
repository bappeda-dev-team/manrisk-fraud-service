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

    @NotNull(message = "Nama risiko tidak boleh koson!")
    @Column(name = "nama_risiko")
    private String namaRisiko;

    @NotNull(message = "Kolom penyebab tidak boleh kosong!")
    @Column(name = "penyebab")
    private String penyebab;

    @NotNull(message = "Kolom akbiat tidak boleh kosong!")
    @Column(name = "akibat")
    private String akibat;

    @NotNull(message = "Skala dampak tidak boleh kosong!")
    @Min(value = 1, message = "Skala dampak minimal 1")
    @Max(value = 5, message = "Skala dampak maksimal 5")
    @Column(name = "skala_dampak")
    private int skalaDampak;

    @NotNull(message = "Skala kemungkinan tidak boleh kosong!")
    @Min(value = 1, message = "Skala kemungkinan minimal 1")
    @Max(value = 5, message = "Skala kemungkinan maksimal 5")
    @Column(name = "skala_kemungkinan")
    private int skalaKemungkinan;

    @Column(name = "status")
    private String status;

    @Column(name = "keterangan")
    private String keterangan;
}
