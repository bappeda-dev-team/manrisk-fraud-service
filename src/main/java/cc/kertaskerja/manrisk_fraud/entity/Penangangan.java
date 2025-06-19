package cc.kertaskerja.manrisk_fraud.entity;

import cc.kertaskerja.manrisk_fraud.common.BaseAuditable;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "penanganan")
public class Penangangan extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_rekin")
    private String idRekin;

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

    @Column(name = "status")
    private String status;

    @Column(name = "keterangan")
    private String keterangan;

    // Default constructor
    public Penangangan() {}

    // All-args constructor
    public Penangangan(String idRekin, String existingControl, String jenisPerlakuanRisiko,
                       String rencanaPerlakuanRisiko, String biayaPerlakuanRisiko,
                       String targetWaktu, String pic, String status, String keterangan) {
        this.idRekin = idRekin;
        this.existingControl = existingControl;
        this.jenisPerlakuanRisiko = jenisPerlakuanRisiko;
        this.rencanaPerlakuanRisiko = rencanaPerlakuanRisiko;
        this.biayaPerlakuanRisiko = biayaPerlakuanRisiko;
        this.targetWaktu = targetWaktu;
        this.pic = pic;
        this.status = status;
        this.keterangan = keterangan;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdRekin() { return idRekin; }
    public void setIdRekin(String idRekin) { this.idRekin = idRekin; }

    public String getExistingControl() { return existingControl; }
    public void setExistingControl(String existingControl) { this.existingControl = existingControl; }

    public String getJenisPerlakuanRisiko() { return jenisPerlakuanRisiko; }
    public void setJenisPerlakuanRisiko(String jenisPerlakuanRisiko) { this.jenisPerlakuanRisiko = jenisPerlakuanRisiko; }

    public String getRencanaPerlakuanRisiko() { return rencanaPerlakuanRisiko; }
    public void setRencanaPerlakuanRisiko(String rencanaPerlakuanRisiko) { this.rencanaPerlakuanRisiko = rencanaPerlakuanRisiko; }

    public String getBiayaPerlakuanRisiko() { return biayaPerlakuanRisiko; }
    public void setBiayaPerlakuanRisiko(String biayaPerlakuanRisiko) { this.biayaPerlakuanRisiko = biayaPerlakuanRisiko; }

    public String getTargetWaktu() { return targetWaktu; }
    public void setTargetWaktu(String targetWaktu) { this.targetWaktu = targetWaktu; }

    public String getPic() { return pic; }
    public void setPic(String pic) { this.pic = pic; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    // Manual Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String idRekin;
        private String existingControl;
        private String jenisPerlakuanRisiko;
        private String rencanaPerlakuanRisiko;
        private String biayaPerlakuanRisiko;
        private String targetWaktu;
        private String pic;
        private String status;
        private String keterangan;

        public Builder idRekin(String idRekin) {
            this.idRekin = idRekin;
            return this;
        }

        public Builder existingControl(String existingControl) {
            this.existingControl = existingControl;
            return this;
        }

        public Builder jenisPerlakuanRisiko(String jenisPerlakuanRisiko) {
            this.jenisPerlakuanRisiko = jenisPerlakuanRisiko;
            return this;
        }

        public Builder rencanaPerlakuanRisiko(String rencanaPerlakuanRisiko) {
            this.rencanaPerlakuanRisiko = rencanaPerlakuanRisiko;
            return this;
        }

        public Builder biayaPerlakuanRisiko(String biayaPerlakuanRisiko) {
            this.biayaPerlakuanRisiko = biayaPerlakuanRisiko;
            return this;
        }

        public Builder targetWaktu(String targetWaktu) {
            this.targetWaktu = targetWaktu;
            return this;
        }

        public Builder pic(String pic) {
            this.pic = pic;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder keterangan(String keterangan) {
            this.keterangan = keterangan;
            return this;
        }

        public Penangangan build() {
            return new Penangangan(
                    idRekin,
                    existingControl,
                    jenisPerlakuanRisiko,
                    rencanaPerlakuanRisiko,
                    biayaPerlakuanRisiko,
                    targetWaktu,
                    pic,
                    status,
                    keterangan
            );
        }
    }

    @Override
    public String toString() {
        return "Penangangan{" +
                "id=" + id +
                ", idRekin='" + idRekin + '\'' +
                ", existingControl='" + existingControl + '\'' +
                ", jenisPerlakuanRisiko='" + jenisPerlakuanRisiko + '\'' +
                ", rencanaPerlakuanRisiko='" + rencanaPerlakuanRisiko + '\'' +
                ", biayaPerlakuanRisiko='" + biayaPerlakuanRisiko + '\'' +
                ", targetWaktu='" + targetWaktu + '\'' +
                ", pic='" + pic + '\'' +
                ", status='" + status + '\'' +
                ", keterangan='" + keterangan + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
