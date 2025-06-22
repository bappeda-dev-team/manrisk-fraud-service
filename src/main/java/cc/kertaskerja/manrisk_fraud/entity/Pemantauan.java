package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
@Table(name = "pemantauan")
public class Pemantauan extends BaseAuditable {
    @Id
    @Column(name = "id_rekin", nullable = false, unique = true)
    private String idRekin;

    @Column(name = "pemilik_risiko")
    private String pemilikRisiko;

    @Column(name = "risiko_kecurangan")
    private String risikoKecurangan;

    @Column(name = "deskripsi_kegiatan_pengendalian")
    private String deskripsiKegiatanPengendalian;

    @Column(name = "pic")
    private String pic;

    @Column(name = "rencana_waktu_pelaksanaan")
    private String rencanaWaktuPelaksanaan;

    @Column(name = "realisasi_waktu_pelaksanaan")
    private String realisasiWaktuPelaksanaan;

    @Column(name = "progres_tindak_lanjut")
    private String progresTindakLanjut;

    @Column(name = "bukti_pelaksanaan_tindak_lanjut")
    private String buktiPelaksanaanTidakLanjut;

    @Column(name = "kendala")
    private String kendala;

    @Column(name = "catatan")
    private String catatan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "keterangan")
    private String keterangan;

    @OneToOne(mappedBy = "pemantauan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonManagedReference
    private HasilPemantauan hasilPemantauan;

    public Pemantauan() {}

    public Pemantauan(String idRekin, String pemilikRisiko, String risikoKecurangan, String deskripsiKegiatanPengendalian, String pic, String rencanaWaktuPelaksanaan, String realisasiWaktuPelaksanaan, String progresTindakLanjut, String buktiPelaksanaanTidakLanjut, String kendala, String catatan, StatusEnum status, String keterangan, HasilPemantauan hasilPemantauan) {
        this.idRekin = idRekin;
        this.pemilikRisiko = pemilikRisiko;
        this.risikoKecurangan = risikoKecurangan;
        this.deskripsiKegiatanPengendalian = deskripsiKegiatanPengendalian;
        this.pic = pic;
        this.rencanaWaktuPelaksanaan = rencanaWaktuPelaksanaan;
        this.realisasiWaktuPelaksanaan = realisasiWaktuPelaksanaan;
        this.progresTindakLanjut = progresTindakLanjut;
        this.buktiPelaksanaanTidakLanjut = buktiPelaksanaanTidakLanjut;
        this.kendala = kendala;
        this.catatan = catatan;
        this.status = status;
        this.keterangan = keterangan;
        this.hasilPemantauan = hasilPemantauan;
    }

    public String getIdRekin() {
        return idRekin;
    }

    public void setIdRekin(String idRekin) {
        this.idRekin = idRekin;
    }

    public String getPemilikRisiko() {
        return pemilikRisiko;
    }

    public void setPemilikRisiko(String pemilikRisiko) {
        this.pemilikRisiko = pemilikRisiko;
    }

    public String getRisikoKecurangan() {
        return risikoKecurangan;
    }

    public void setRisikoKecurangan(String risikoKecurangan) {
        this.risikoKecurangan = risikoKecurangan;
    }

    public String getDeskripsiKegiatanPengendalian() {
        return deskripsiKegiatanPengendalian;
    }

    public void setDeskripsiKegiatanPengendalian(String deskripsiKegiatanPengendalian) {
        this.deskripsiKegiatanPengendalian = deskripsiKegiatanPengendalian;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRencanaWaktuPelaksanaan() {
        return rencanaWaktuPelaksanaan;
    }

    public void setRencanaWaktuPelaksanaan(String rencanaWaktuPelaksanaan) {
        this.rencanaWaktuPelaksanaan = rencanaWaktuPelaksanaan;
    }

    public String getRealisasiWaktuPelaksanaan() {
        return realisasiWaktuPelaksanaan;
    }

    public void setRealisasiWaktuPelaksanaan(String realisasiWaktuPelaksanaan) {
        this.realisasiWaktuPelaksanaan = realisasiWaktuPelaksanaan;
    }

    public String getProgresTindakLanjut() {
        return progresTindakLanjut;
    }

    public void setProgresTindakLanjut(String progresTindakLanjut) {
        this.progresTindakLanjut = progresTindakLanjut;
    }

    public String getBuktiPelaksanaanTidakLanjut() {
        return buktiPelaksanaanTidakLanjut;
    }

    public void setBuktiPelaksanaanTidakLanjut(String buktiPelaksanaanTidakLanjut) {
        this.buktiPelaksanaanTidakLanjut = buktiPelaksanaanTidakLanjut;
    }

    public String getKendala() {
        return kendala;
    }

    public void setKendala(String kendala) {
        this.kendala = kendala;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public HasilPemantauan getHasilPemantauan() {
        return hasilPemantauan;
    }

    public void setHasilPemantauan(HasilPemantauan hasilPemantauan) {
        this.hasilPemantauan = hasilPemantauan;
    }

    @Override
    public String toString() {
        return "Pemantauan{" +
                "idRekin='" + idRekin + '\'' +
                ", pemilikRisiko='" + pemilikRisiko + '\'' +
                ", risikoKecurangan='" + risikoKecurangan + '\'' +
                ", deskripsiKegiatanPengendalian='" + deskripsiKegiatanPengendalian + '\'' +
                ", pic='" + pic + '\'' +
                ", rencanaWaktuPelaksanaan='" + rencanaWaktuPelaksanaan + '\'' +
                ", realisasiWaktuPelaksanaan='" + realisasiWaktuPelaksanaan + '\'' +
                ", progresTindakLanjut='" + progresTindakLanjut + '\'' +
                ", buktiPelaksanaanTidakLanjut='" + buktiPelaksanaanTidakLanjut + '\'' +
                ", kendala='" + kendala + '\'' +
                ", catatan='" + catatan + '\'' +
                ", status=" + status +
                ", keterangan='" + keterangan + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
