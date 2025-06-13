package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "identifikasi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(exclude = "identifikasi")
@Builder
public class Manrisk extends BaseAuditable {
    @Id
    @Column(name = "id_manrisk", nullable = false, length = 100)
    private String idManrisk;

    @OneToOne(mappedBy = "idManrisk", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Identifikasi identifikasi;

    @OneToOne(mappedBy = "idManrisk", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Analisa analisa;
}
