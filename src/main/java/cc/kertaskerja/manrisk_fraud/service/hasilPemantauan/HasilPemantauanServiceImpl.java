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
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
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
            .registerModule(new Hibernate6Module())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final RencanaKinerjaService rencanaKinerjaService;


    private HasilPemantauanDTO buildDTOFFromHasilPemantauan(JsonNode rk, JsonNode pemantauan, HasilPemantauan h) {
        System.out.println("pemantauan JSON: " + pemantauan.toPrettyString());

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
                .pemilik_risiko(pemantauan.path("pemilikRisiko").asText())
                .risiko_kecurangan(pemantauan.path("risikoKecurangan").asText())
                .deskripsi_kegiatan_pengendalian(pemantauan.path("deskripsiKegiatanPengendalian").asText())
                .pic(pemantauan.path("pic").asText())
                .rencana_waktu_pelaksanaan(pemantauan.path("rencanaWaktuPelaksanaan").asText())
                .realisasi_waktu_pelaksanaan(pemantauan.path("realisasiWaktuPelaksanaan").asText())
                .progres_tindak_lanjut(pemantauan.path("progresTindakLanjut").asText())
                .bukti_pelaksanaan_tindak_lanjut(pemantauan.path("buktiPelaksanaanTidakLanjut").asText())
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
                .pemilik_risiko(pemantauan.path("pemilikRisiko").asText())
                .risiko_kecurangan(pemantauan.path("risikoKecurangan").asText())
                .deskripsi_kegiatan_pengendalian(pemantauan.path("deskripsiKegiatanPengendalian").asText())
                .pic(pemantauan.path("pic").asText())
                .rencana_waktu_pelaksanaan(pemantauan.path("rencanaWaktuPelaksanaan").asText())
                .realisasi_waktu_pelaksanaan(pemantauan.path("realisasiWaktuPelaksanaan").asText())
                .progres_tindak_lanjut(pemantauan.path("progresTindakLanjut").asText())
                .bukti_pelaksanaan_tindak_lanjut(pemantauan.path("buktiPelaksanaanTidakLanjut").asText())
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
        List<HasilPemantauan> hasilPemantauanList = hpRepository.findAll();

        // Group Pemantauan by idRekin
        Map<String, List<Pemantauan>> pemantauanMap = new HashMap<>();
        for (Pemantauan p : pemantauanList) {
            pemantauanMap.computeIfAbsent(p.getIdRencanaKinerja(), k -> new ArrayList<>()).add(p);
        }

        // Group HasilPemantauan by idRekin
        Map<String, List<HasilPemantauan>> hasilPemantauanMap = new HashMap<>();
        for (HasilPemantauan h : hasilPemantauanList) {
            hasilPemantauanMap.computeIfAbsent(h.getIdRencanaKinerja(), k -> new ArrayList<>()).add(h);
        }

        List<HasilPemantauanDTO> result = new ArrayList<>();

        for (Map<String, Object> rekinDetail : rekinList) {
            JsonNode rkNode = objectMapper.convertValue(rekinDetail, JsonNode.class);
            String idRekin = rkNode.path("id_rencana_kinerja").asText();

            List<Pemantauan> pemantauanEntries = pemantauanMap.getOrDefault(idRekin, new ArrayList<>());

            // Only process if Pemantauan exists
            if (!pemantauanEntries.isEmpty()) {
                List<HasilPemantauan> hasilEntries = hasilPemantauanMap.getOrDefault(idRekin, new ArrayList<>());

                if (!hasilEntries.isEmpty()) {
                    for (Pemantauan p : pemantauanEntries) {
                        for (HasilPemantauan h : hasilEntries) {
                            result.add(buildDTOFFromHasilPemantauan(rkNode, objectMapper.convertValue(p, JsonNode.class), h));
                        }
                    }
                } else {
                    for (Pemantauan p : pemantauanEntries) {
                        result.add(buildDTOFFromRkOnly(rkNode, objectMapper.convertValue(p, JsonNode.class)));
                    }
                }
            }

            // If there's no Pemantauan, skip. (Do not add anything)
        }

        return result;
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

        if (hpRepository.existsByIdRencanaKinerja(idRekin)) {
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
