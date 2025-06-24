package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.helper.PegawaiInfoConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "penanganan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Builder
public class Penangangan extends BaseAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_rencana_kinerja")
    private String idRencanaKinerja;

    @Column(name = "existing_control")
    private String existingControl;

    @Column(name = "jenis_perlakuan_risiko")
    private String jenisPerlakuanRisiko;

    @Column(name = "rencana_perlakuan_risiko")
    private String rencanaPerlakuanRisiko;

    @Column(name = "biaya_perlakuan_risiko")
    private String biayaPerlakuanRisiko;

    @Column(name = "target_waktu")
    private String targetWaktu;

    @Column(name = "pic")
    private String pic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "keterangan")
    private String keterangan;

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
