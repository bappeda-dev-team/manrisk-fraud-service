package cc.kertaskerja.manrisk_fraud.dto.identifikasi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdentifikasiReqDTO {

    @NotBlank(message = "Nama risiko wajib diisi")
    @JsonProperty("nama_risiko")
    private String namaRisiko;

    @NotBlank(message = "Jenis risiko wajib diisi")
    @JsonProperty("jenis_risiko")
    private String jenisRisiko;

    @NotBlank(message = "Kemungkinan kecurangan wajib diisi")
    @JsonProperty("kemungkinan_kecurangan")
    private String kemungkinanKecurangan;

    @NotBlank(message = "Indikasi wajib diisi")
    @JsonProperty("indikasi")
    private String indikasi;

    @NotBlank(message = "Kemungkinan pihak terkait wajib diisi")
    @JsonProperty("kemungkinan_pihak_terkait")
    private String kemungkinanPihakTerkait;

    @JsonProperty("keterangan")
    private String keterangan;

    @NotBlank(message = "ID Manrisk wajib diisi")
    @JsonProperty("id_manrisk")
    private String idManrisk;

    public IdentifikasiReqDTO(String namaRisiko, String jenisRisiko, String kemungkinanKecurangan, String indikasi, String kemungkinanPihakTerkait, String keterangan, String idManrisk) {
        this.namaRisiko = namaRisiko;
        this.jenisRisiko = jenisRisiko;
        this.kemungkinanKecurangan = kemungkinanKecurangan;
        this.indikasi = indikasi;
        this.kemungkinanPihakTerkait = kemungkinanPihakTerkait;
        this.keterangan = keterangan;
        this.idManrisk = idManrisk;
    }

    public String getNamaRisiko() {
        return namaRisiko;
    }

    public void setNamaRisiko(String namaRisiko) {
        this.namaRisiko = namaRisiko;
    }

    public String getJenisRisiko() {
        return jenisRisiko;
    }

    public void setJenisRisiko(String jenisRisiko) {
        this.jenisRisiko = jenisRisiko;
    }

    public String getKemungkinanKecurangan() {
        return kemungkinanKecurangan;
    }

    public void setKemungkinanKecurangan(String kemungkinanKecurangan) {
        this.kemungkinanKecurangan = kemungkinanKecurangan;
    }

    public String getIndikasi() {
        return indikasi;
    }

    public void setIndikasi(String indikasi) {
        this.indikasi = indikasi;
    }

    public String getKemungkinanPihakTerkait() {
        return kemungkinanPihakTerkait;
    }

    public void setKemungkinanPihakTerkait(String kemungkinanPihakTerkait) {
        this.kemungkinanPihakTerkait = kemungkinanPihakTerkait;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getIdManrisk() {
        return idManrisk;
    }

    public void setIdManrisk(String idManrisk) {
        this.idManrisk = idManrisk;
    }

    @Override
    public String toString() {
        return "IdentifikasiReqDTO{" +
                "namaRisiko='" + namaRisiko + '\'' +
                ", jenisRisiko='" + jenisRisiko + '\'' +
                ", kemungkinanKecurangan='" + kemungkinanKecurangan + '\'' +
                ", indikasi='" + indikasi + '\'' +
                ", kemungkinanPihakTerkait='" + kemungkinanPihakTerkait + '\'' +
                ", keterangan='" + keterangan + '\'' +
                ", idManrisk='" + idManrisk + '\'' +
                '}';
    }
}
