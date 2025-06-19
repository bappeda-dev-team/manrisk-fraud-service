package cc.kertaskerja.manrisk_fraud.service.pemantauan;

import cc.kertaskerja.manrisk_fraud.dto.PemantauanDTO;
import cc.kertaskerja.manrisk_fraud.entity.Pemantauan;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.repository.PemantauanRepository;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PemantauanServiceImpl implements PemantauanService {

    private final PemantauanRepository pemantauanRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private PemantauanDTO buildDTOFFromRkAndPemantauan(JsonNode rk, Pemantauan p) {
        return PemantauanDTO.builder()
                .id(p.getId())
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(PemantauanDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .risiko_kecurangan(p.getRisikoKecurangan())
                .deskripsi_kegiatan_pengendalian(p.getDeskripsiKegiatanPengendalian())
                .pic(p.getPic())
                .rencana_waktu_pelaksanaan(p.getRencanaWaktuPelaksanaan())
                .realisasi_waktu_pelaksanaan(p.getRealisasiWaktuPelaksanaan())
                .progres_tindak_lanjut(p.getProgresTindakLanjut())
                .skala_dampak(p.getSkalaDampak())
                .skala_kemungkinan(p.getSkalaKemungkinan())
                .tingkat_risiko(p.getTingkatRisiko())
                .level_risiko(p.getLevelRisiko())
                .bukti_pelaksanaan(p.getBuktiPelaksanaan())
                .kendala(p.getKendala())
                .catatan(p.getCatatan())
                .status(p.getStatus() != null ? p.getStatus().name() : null)
                .keterangan(p.getKeterangan())
                .created_at(p.getCreatedAt())
                .updated_at(p.getUpdatedAt())
                .build();
    }

    private PemantauanDTO buildDTOFFromRkOnly(JsonNode rk) {
        return PemantauanDTO.builder()
                .id(null)
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(PemantauanDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .risiko_kecurangan("")
                .deskripsi_kegiatan_pengendalian("")
                .pic("")
                .rencana_waktu_pelaksanaan("")
                .realisasi_waktu_pelaksanaan("")
                .progres_tindak_lanjut("")
                .skala_dampak(0)
                .skala_kemungkinan(0)
                .tingkat_risiko(0)
                .level_risiko("")
                .bukti_pelaksanaan("")
                .kendala("")
                .catatan("")
                .status("")
                .keterangan("")
                .build();

    }

    @Override
    public List<PemantauanDTO> findAllPemantauan(String nip, String tahun) {
        Map<String, Object> externalResponse = rencanaKinerjaService.getRencanaKinerja(nip, tahun);
        Object rkObj = externalResponse.get("rencana_kinerja");

        if (!(rkObj instanceof List)) {
            throw new ResourceNotFoundException("No 'Rencana Kinerja' data found for NIP: " + nip + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkObj;
        List<Pemantauan> pemantauanList = pemantauanRepository.findAll();

        Map<String, List<Pemantauan>> pemantauanMap = new HashMap<>();
        for (Pemantauan p : pemantauanList) {
            String idRekin = p.getIdRekin();
            pemantauanMap.computeIfAbsent(idRekin, k -> new ArrayList<>()).add(p);
        }

        List<PemantauanDTO> result = new ArrayList<>();

        for (Map<String, Object> rk : rekinList) {
            JsonNode rkNode = objectMapper.convertValue(rk, JsonNode.class);
            String idRekin = rkNode.path("id_rencana_kinerja").asText();

            List<Pemantauan> tempList = pemantauanMap.get(idRekin);

            if (tempList != null && !tempList.isEmpty()) {
                for (Pemantauan p : tempList) {
                    result.add(buildDTOFFromRkAndPemantauan(rkNode, p));
                }
            } else {
                result.add(buildDTOFFromRkOnly(rkNode));
            }
        }

        return result;
    }

    @Override
    public PemantauanDTO savePemantauan(PemantauanDTO pemantauanDTO) {
        String idRekin = pemantauanDTO.getId_rencana_kinerja();

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("id_rencana_kinerja is required");
        }

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        if (pemantauanRepository.existsByIdRekin(idRekin)) {
            throw new ResourceNotFoundException("Data identifikasi already exists for id_rencana_kinerja: " + idRekin);
        }

        int tingkatRisiko = pemantauanDTO.getSkala_dampak() * pemantauanDTO.getSkala_kemungkinan();

        String levelRisiko;
        if (tingkatRisiko >= 1 && tingkatRisiko <= 4) {
            levelRisiko = "Rendah";
        } else if (tingkatRisiko >= 5 && tingkatRisiko <= 12) {
            levelRisiko = "Menengah";
        } else if (tingkatRisiko >= 15 && tingkatRisiko <= 25) {
            levelRisiko = "Tinggi";
        } else {
            levelRisiko = "-";
        }

        Pemantauan pemantauan = Pemantauan.builder()
                .idRekin(idRekin)
                .risikoKecurangan(pemantauanDTO.getRisiko_kecurangan())
                .deskripsiKegiatanPengendalian(pemantauanDTO.getDeskripsi_kegiatan_pengendalian())
                .pic(pemantauanDTO.getPic())
                .rencanaWaktuPelaksanaan(pemantauanDTO.getRencana_waktu_pelaksanaan())
                .realisasiWaktuPelaksanaan(pemantauanDTO.getRealisasi_waktu_pelaksanaan())
                .progresTindakLanjut(pemantauanDTO.getProgres_tindak_lanjut())
                .skalaDampak(pemantauanDTO.getSkala_dampak())
                .skalaKemungkinan(pemantauanDTO.getSkala_kemungkinan())
                .tingkatRisiko(tingkatRisiko)
                .levelRisiko(levelRisiko)
                .buktiPelaksanaan(pemantauanDTO.getBukti_pelaksanaan())
                .kendala(pemantauanDTO.getKendala())
                .catatan(pemantauanDTO.getCatatan())
                .status(StatusEnum.PENDING)
                .build();

        Pemantauan saved = pemantauanRepository.save(pemantauan);

        return buildDTOFFromRkAndPemantauan(rkNode, saved);
    }
}
