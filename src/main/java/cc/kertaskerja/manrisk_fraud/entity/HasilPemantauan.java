package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hasil_pemantauan")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
public class HasilPemantauan extends BaseAuditable {
    @Id
    @Column(name = "id_rencana_kinerja")
    private String idRencanaKinerja;

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
    @JoinColumn(name = "id_rencana_kinerja", referencedColumnName = "id_rencana_kinerja")
    @JsonBackReference
    private Pemantauan pemantauan;

    public HasilPemantauan() {}

    public HasilPemantauan(String idRencanaKinerja, int skalaDampak, int skalaKemungkinan, Integer tingkatRisiko, String levelRisiko, Pemantauan pemantauan) {
        this.idRencanaKinerja = idRencanaKinerja;
        this.skalaDampak = skalaDampak;
        this.skalaKemungkinan = skalaKemungkinan;
        this.tingkatRisiko = tingkatRisiko;
        this.levelRisiko = levelRisiko;
        this.pemantauan = pemantauan;
    }

    public String getIdRencanaKinerja() {
        return idRencanaKinerja;
    }

    public void setIdRencanaKinerja(String idRencanaKinerja) {
        this.idRencanaKinerja = idRencanaKinerja;
    }

    public int getSkalaDampak() {
        return skalaDampak;
    }

    public void setSkalaDampak(int skalaDampak) {
        this.skalaDampak = skalaDampak;
    }

    public int getSkalaKemungkinan() {
        return skalaKemungkinan;
    }

    public void setSkalaKemungkinan(int skalaKemungkinan) {
        this.skalaKemungkinan = skalaKemungkinan;
    }

    public Integer getTingkatRisiko() {
        return tingkatRisiko;
    }

    public void setTingkatRisiko(Integer tingkatRisiko) {
        this.tingkatRisiko = tingkatRisiko;
    }

    public String getLevelRisiko() {
        return levelRisiko;
    }

    public void setLevelRisiko(String levelRisiko) {
        this.levelRisiko = levelRisiko;
    }

    public Pemantauan getPemantauan() {
        return pemantauan;
    }

    public void setPemantauan(Pemantauan pemantauan) {
        this.pemantauan = pemantauan;
    }

    @Override
    public String toString() {
        return "HasilPemantauan{" +
                "idRencanaKinerja='" + idRencanaKinerja + '\'' +
                ", skalaDampak=" + skalaDampak +
                ", skalaKemungkinan=" + skalaKemungkinan +
                ", tingkatRisiko=" + tingkatRisiko +
                ", levelRisiko='" + levelRisiko + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

