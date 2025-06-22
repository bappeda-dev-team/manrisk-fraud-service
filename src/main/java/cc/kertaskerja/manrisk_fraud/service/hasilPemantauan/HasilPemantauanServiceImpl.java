package cc.kertaskerja.manrisk_fraud.service.hasilPemantauan;

import cc.kertaskerja.manrisk_fraud.dto.HasilPemantauanDTO;
import cc.kertaskerja.manrisk_fraud.entity.HasilPemantauan;
import cc.kertaskerja.manrisk_fraud.entity.Pemantauan;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.repository.HasilPemantauanRepository;
import cc.kertaskerja.manrisk_fraud.repository.PemantauanRepository;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HasilPemantauanServiceImpl implements HasilPemantauanService {

    private final RencanaKinerjaService rekinService;
    private final PemantauanRepository pemantauanRepository;
    private final HasilPemantauanRepository hpRepository;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final RencanaKinerjaService rencanaKinerjaService;


    private HasilPemantauanDTO buildDTOFFromHasilPemantauan(JsonNode rk, JsonNode pemantauan, HasilPemantauan h) {
        return HasilPemantauanDTO.builder()
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(HasilPemantauanDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .pemilik_risiko(pemantauan.path("pemilik_risiko").asText())
                .risiko_kecurangan(pemantauan.path("risiko_kecurangan").asText())
                .deskripsi_kegiatan_pengendalian(pemantauan.path("deskripsi_kegiatan_pengendalian").asText())
                .pic(pemantauan.path("pic").asText())
                .rencana_waktu_pelaksanaan(pemantauan.path("rencana_waktu_pelaksanaan").asText())
                .realisasi_waktu_pelaksanaan(pemantauan.path("realisasi_waktu_pelaksanaan").asText())
                .progres_tindak_lanjut(pemantauan.path("progres_tindak_lanjut").asText())
                .bukti_pelaksanaan_tindak_lanjut(pemantauan.path("bukti_pelaksanaan_tindak_lanjut").asText())
                .kendala(pemantauan.path("kendala").asText())
                .skala_dampak(h.getSkalaDampak())
                .skala_kemungkinan(h.getSkalaKemungkinan())
                .tingkat_risiko(h.getTingkatRisiko())
                .level_risiko(h.getLevelRisiko())
                .catatan(pemantauan.path("catatan").asText())
                .status(pemantauan.path("status").asText())
                .created_at(h.getCreatedAt())
                .updated_at(h.getUpdatedAt())
                .build();
    }

    private HasilPemantauanDTO buildDTOFFromRkOnly(JsonNode rk, JsonNode pemantauan) {
        return HasilPemantauanDTO.builder()
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(HasilPemantauanDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .pemilik_risiko(pemantauan.path("pemilik_risiko").asText())
                .risiko_kecurangan(pemantauan.path("risiko_kecurangan").asText())
                .deskripsi_kegiatan_pengendalian(pemantauan.path("deskripsi_kegiatan_pengendalian").asText())
                .pic(pemantauan.path("pic").asText())
                .rencana_waktu_pelaksanaan(pemantauan.path("rencana_waktu_pelaksanaan").asText())
                .realisasi_waktu_pelaksanaan(pemantauan.path("realisasi_waktu_pelaksanaan").asText())
                .progres_tindak_lanjut(pemantauan.path("progres_tindak_lanjut").asText())
                .bukti_pelaksanaan_tindak_lanjut(pemantauan.path("bukti_pelaksanaan_tindak_lanjut").asText())
                .kendala(pemantauan.path("kendala").asText())
                .skala_dampak(0)
                .skala_kemungkinan(0)
                .tingkat_risiko(0)
                .level_risiko("")
                .catatan(pemantauan.path("catatan").asText())
                .status(pemantauan.path("status").asText())
                .build();
    }

    @Override
    public List<HasilPemantauanDTO> findAll(String nip, String tahun) {
        Map<String, Object> rekin = rencanaKinerjaService.getRencanaKinerja(nip, tahun);
        Object rkObj = rekin.get("rencana_kinerja");

        if (!(rkObj instanceof List)) {
            throw new ResourceNotFoundException("No 'Rencana Kinerja' data found for NIP: " + nip + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkObj;
        List<Pemantauan> pemantauanList = pemantauanRepository.findAll();

        Map<String, List<Pemantauan>> pemantauanMap = new HashMap<>();
        for (int i = 0; i < pemantauanList.size(); i++) {
            Pemantauan p = pemantauanList.get(i);
            String idRekin = p.getIdRekin();

            if (!pemantauanMap.containsKey(idRekin)) {
                pemantauanMap.put(idRekin, new ArrayList<>());
            }

            pemantauanMap.get(idRekin).add(p);
        }

        List<HasilPemantauan> hasilPemantauanList = hpRepository.findAll();

        Map<String, List<HasilPemantauan>> hasilPemantauanMap = new HashMap<>();
        for (int i = 0; i < hasilPemantauanList.size(); i++) {
            HasilPemantauan hp = hasilPemantauanList.get(i);
            String idRekin = hp.getIdRekin();

            if (!hasilPemantauanMap.containsKey(idRekin)) {
                hasilPemantauanMap.put(idRekin, new ArrayList<>());
            }

            hasilPemantauanMap.get(idRekin).add(hp);
        }

        List<HasilPemantauanDTO> hasilPemantauanDTOList = new ArrayList<>();

        Pemantauan tempPemantauan = null;
        HasilPemantauan tempHasilPemantauan = null;

        List<HasilPemantauanDTO> result = new ArrayList<>();

        for (int i = 0; i < rekinList.size(); i++) {
            Map<String, Object> rekinDetail = rekinList.get(i);
            JsonNode rkNode = objectMapper.convertValue(rekinDetail, JsonNode.class);
            String idRekin = rkNode.path("id_rencana_kinerja").asText();

            List<Pemantauan> pMap = pemantauanMap.get(idRekin);
            if (pMap != null && pMap.size() > 0) {
                for (int j = 0; j < pMap.size(); j++) {
                    tempPemantauan = pMap.get(j);
                }
            }

            List<HasilPemantauan> hpMap = hasilPemantauanMap.get(idRekin);
            if (hpMap != null && hpMap.size() > 0) {
                for (int j = 0; j < hpMap.size(); j++) {
                    tempHasilPemantauan = hpMap.get(j);
                }
            }
        }

        return List.of();
    }

    @Override
    @Transactional
    public HasilPemantauanDTO save(HasilPemantauanDTO dto) {
        String idRekin = dto.getId_rencana_kinerja();

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("id_rencana_kinerja is required");
        }

        if (idRekin == null || idRekin.isBlank()) {
            throw new IllegalArgumentException("idRekin tidak boleh null/empty");
        }

        Map<String, Object> rekinDetail = rekinService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }
        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        Pemantauan pemantauan = pemantauanRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for id_rencana_kinerja: " + idRekin));
        JsonNode pemantauanNode = objectMapper.convertValue(pemantauan, JsonNode.class);

        if (hpRepository.existsByIdRekin(idRekin)) {
            throw new ResourceNotFoundException("Data hasil pemantauan already exists for id_rencana_kinerja: " + idRekin);
        }

        int tingkatRisiko = (dto.getSkala_kemungkinan() * dto.getSkala_dampak());
        String levelRisiko = tingkatRisiko <= 4 ? "Rendah"
                : tingkatRisiko <= 12 ? "Menengah"
                : tingkatRisiko <= 25 ? "Tinggi"
                : "-";

        HasilPemantauan hasilPemantauan = HasilPemantauan.builder()
                .skalaDampak(dto.getSkala_dampak())
                .skalaKemungkinan(dto.getSkala_kemungkinan())
                .tingkatRisiko(tingkatRisiko)
                .levelRisiko(levelRisiko)
                .pemantauan(pemantauan)
                .build();

        HasilPemantauan saved = hpRepository.save(hasilPemantauan);

        return buildDTOFFromHasilPemantauan(rkNode, pemantauanNode, saved);
    }
}
