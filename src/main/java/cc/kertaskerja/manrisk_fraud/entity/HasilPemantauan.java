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
    @JsonBackReference
    private Pemantauan pemantauan;

    public HasilPemantauan() {}

    public HasilPemantauan(String idRekin, int skalaDampak, int skalaKemungkinan, Integer tingkatRisiko, String levelRisiko, Pemantauan pemantauan) {
        this.idRekin = idRekin;
        this.skalaDampak = skalaDampak;
        this.skalaKemungkinan = skalaKemungkinan;
        this.tingkatRisiko = tingkatRisiko;
        this.levelRisiko = levelRisiko;
        this.pemantauan = pemantauan;
    }

    public String getIdRekin() {
        return idRekin;
    }

    public void setIdRekin(String idRekin) {
        this.idRekin = idRekin;
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
                "idRekin='" + idRekin + '\'' +
                ", skalaDampak=" + skalaDampak +
                ", skalaKemungkinan=" + skalaKemungkinan +
                ", tingkatRisiko=" + tingkatRisiko +
                ", levelRisiko='" + levelRisiko + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

