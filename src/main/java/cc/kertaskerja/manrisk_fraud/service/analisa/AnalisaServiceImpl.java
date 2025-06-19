package cc.kertaskerja.manrisk_fraud.service.analisa;

import cc.kertaskerja.manrisk_fraud.dto.AnalisaDTO;
import cc.kertaskerja.manrisk_fraud.dto.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.entity.Analisa;
import cc.kertaskerja.manrisk_fraud.exception.InternalServerException;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.repository.AnalisaRepository;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalisaServiceImpl implements AnalisaService {

    private final AnalisaRepository analisaRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private AnalisaDTO buildTOFFromRkAndAnalisa(JsonNode rk, Analisa analisa) {
        int tingkatRisiko = analisa.getSkalaKemungkinan() * analisa.getSkalaDampak();

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

        return AnalisaDTO.builder()
                .id(analisa.getId())
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(AnalisaDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .nama_risiko(analisa.getNamaRisiko())
                .penyebab(analisa.getPenyebab())
                .akibat(analisa.getAkibat())
                .skala_dampak(analisa.getSkalaDampak())
                .skala_kemungkinan(analisa.getSkalaKemungkinan())
                .tingkat_risiko(tingkatRisiko)
                .level_risiko(levelRisiko)
                .status(analisa.getStatus())
                .keterangan(analisa.getKeterangan())
                .created_at(analisa.getCreatedAt())
                .updated_at(analisa.getUpdatedAt())
                .build();
    }

    private AnalisaDTO buildTOFFromRkOnly(JsonNode rk) {
        return AnalisaDTO.builder()
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
                    .operasional_daerah(AnalisaDTO.OperasionalDaerah.builder()
                            .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                            .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                            .build())
                    .nama_risiko("")
                    .penyebab("")
                    .akibat("")
                    .skala_dampak(0)
                    .skala_kemungkinan(0)
                .tingkat_risiko(0)
                .level_risiko("")
                    .status("")
                    .keterangan("")
                    .created_at(null)
                    .updated_at(null)
                    .build();
    }

    @Override
    public List<AnalisaDTO> findAllAnalisa(String nip, String tahun) {
        Map<String, Object> externalResponse = rencanaKinerjaService.getRencanaKinerja(nip, tahun);
        Object rkObj = externalResponse.get("rencana_kinerja");

        if (!(rkObj instanceof List<?> rkList)) {
            throw new ResourceNotFoundException("No 'rencana_kinerja' data found for NIP: " + nip + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkList;

        List<Analisa> analisaList = analisaRepository.findAll();
        Map<String, List<Analisa>> analisaMap = analisaList.stream()
                .collect(Collectors.groupingBy(Analisa::getIdRekin));

        List<AnalisaDTO> result = new ArrayList<>();

        for (Map<String, Object> rk : rekinList) {
            JsonNode rkNode = objectMapper.convertValue(rk, JsonNode.class);
            String idRencana = rkNode.get("id_rencana_kinerja").asText();
            List<Analisa> temp = analisaMap.getOrDefault(idRencana, List.of());

            if (!temp.isEmpty()) {
                for (Analisa analisa : temp) {
                    result.add(buildTOFFromRkAndAnalisa(rkNode, analisa));
                }
            } else {
                result.add(buildTOFFromRkOnly(rkNode));
            }
        }

       return result;
    }

    @Override
    public AnalisaDTO findOneAnalisa(String idRekin) {
       Map<String, Object> detailResponse = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
       Object rkObj = detailResponse.get("rencana_kinerja");

        if (rkObj == null) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        return analisaRepository.findOneByIdRekin(idRekin)
                .map(a -> buildTOFFromRkAndAnalisa(rkNode, a))
                .orElseGet(() -> buildTOFFromRkOnly(rkNode));
    }

    @Override
    public AnalisaDTO saveAnalisa(AnalisaDTO analisaDTO) {
        String idRekin = analisaDTO.getId_rencana_kinerja();

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("ID rekin tidak ditemukan");
        }

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        if (analisaRepository.existsByIdRekin(idRekin)) {
            throw new InternalServerException("Data identifikasi already exists for id_rencana_kinerja: " + idRekin);
        }

        Analisa analisa = Analisa.builder()
                .idRekin(idRekin)
                .namaRisiko(analisaDTO.getNama_risiko())
                .penyebab(analisaDTO.getPenyebab())
                .akibat(analisaDTO.getAkibat())
                .skalaDampak(analisaDTO.getSkala_dampak())
                .skalaKemungkinan(analisaDTO.getSkala_kemungkinan())
                .status("Pending")
                .keterangan(analisaDTO.getKeterangan())
                .build();

        Analisa saved = analisaRepository.save(analisa);

        return buildTOFFromRkAndAnalisa(rkNode, saved);
    }

    @Override
    @Transactional
    public AnalisaDTO updateAnalisa(String idRekin, AnalisaDTO analisaDTO) {
        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        Analisa analisa = analisaRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for ID Rencana Kinerja: " + idRekin));

        analisa.setNamaRisiko(analisaDTO.getNama_risiko());
        analisa.setPenyebab(analisaDTO.getPenyebab());
        analisa.setAkibat(analisaDTO.getAkibat());
        analisa.setSkalaDampak(analisaDTO.getSkala_dampak());
        analisa.setSkalaKemungkinan(analisaDTO.getSkala_kemungkinan());
        analisa.setKeterangan(analisaDTO.getKeterangan());

        Analisa updated = analisaRepository.save(analisa);

        Map<String, Object> detailResponse = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        JsonNode rkNode = objectMapper.convertValue(detailResponse.get("rencana_kinerja"), JsonNode.class);

        return buildTOFFromRkAndAnalisa(rkNode, updated);
    }

    @Override
    public void deleteAnalisa(String idRekin) {
        Analisa analisa = analisaRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Identifikasi not found for id_rencana_kinerja: " + idRekin));

        analisaRepository.delete(analisa);
    }
}
