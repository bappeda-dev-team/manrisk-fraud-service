package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.Text;

@Entity
@Table (name = "risiko_kecurangan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class RisikoKecurangan extends BaseAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "jenis_risiko", nullable = false, length = 255)
    private String jenisRisiko;

    @Column(name = "uraian", columnDefinition = "TEXT")
    private String uraian;
}
