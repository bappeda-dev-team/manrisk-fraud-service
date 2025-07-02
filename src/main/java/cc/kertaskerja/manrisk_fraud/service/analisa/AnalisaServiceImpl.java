package cc.kertaskerja.manrisk_fraud.service.analisa;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaResDTO;
import cc.kertaskerja.manrisk_fraud.entity.Analisa;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.exception.BadRequestException;
import cc.kertaskerja.manrisk_fraud.exception.InternalServerException;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.helper.Authorization;
import cc.kertaskerja.manrisk_fraud.helper.Crypto;
import cc.kertaskerja.manrisk_fraud.repository.AnalisaRepository;
import cc.kertaskerja.manrisk_fraud.service.global.PegawaiService;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalisaServiceImpl implements AnalisaService {

    private final AnalisaRepository analisaRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final PegawaiService pegawaiService;
    private final ObjectMapper objectMapper;

    private AnalisaResDTO buildTOFFromRkAndAnalisa(JsonNode rk, Analisa analisa) {
        return AnalisaResDTO.builder()
                .id(analisa.getId())
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText(null))
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText(null))
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText(null))
                .tahun(rk.path("tahun").asText(null))
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText(null))
                .pegawai_id(rk.path("pegawai_id").asText(null))
                .nama_pegawai(rk.path("nama_pegawai").asText(null))
                .operasional_daerah(AnalisaResDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText(null))
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText(null))
                        .build())
                .nama_risiko(analisa.getNamaRisiko())
                .penyebab(analisa.getPenyebab())
                .akibat(analisa.getAkibat())
                .skala_dampak(analisa.getSkalaDampak())
                .skala_kemungkinan(analisa.getSkalaKemungkinan())
                .tingkat_risiko(analisa.getTingkatRisiko())
                .level_risiko(analisa.getLevelRisiko())
                .status(analisa.getStatus() != null ? analisa.getStatus().name() : null)
                .pembuat(analisa.getPembuat())
                .verifikator(analisa.getVerifikator())
                .keterangan(analisa.getKeterangan())
                .build();
    }

    private AnalisaResDTO buildTOFFromRkOnly(JsonNode rk) {
        return AnalisaResDTO.builder()
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
                .operasional_daerah(AnalisaResDTO.OperasionalDaerah.builder()
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
                .build();
    }

    @Override
    public List<AnalisaResDTO> findAllAnalisa(String nip, String tahun) {
        if (!Crypto.isEncrypted(nip)) {
            throw new ResourceNotFoundException("NIP is not encrypted: " + nip);
        }

        Map<String, Object> externalResponse = rencanaKinerjaService.getRencanaKinerja(Crypto.decrypt(nip), tahun);
        Object rkObj = externalResponse.get("rencana_kinerja");

        if (!(rkObj instanceof List<?> rkList)) {
            throw new ResourceNotFoundException("No 'rencana_kinerja' data found for NIP: " + Crypto.decrypt(nip) + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkList;

        List<Analisa> analisaList = analisaRepository.findAll();
        Map<String, List<Analisa>> analisaMap = analisaList.stream()
                .collect(Collectors.groupingBy(Analisa::getIdRencanaKinerja));

        List<AnalisaResDTO> result = new ArrayList<>();

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
    public AnalisaResDTO findOneAnalisa(String idRekin) {
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
    @Transactional
    public AnalisaResDTO saveAnalisa(AnalisaReqDTO reqDTO) {
        String idRekin = reqDTO.getId_rencana_kinerja();

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("ID rekin tidak ditemukan");
        }

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        if (analisaRepository.existsByIdRencanaKinerja(idRekin)) {
            throw new InternalServerException("Data identifikasi already exists for id_rencana_kinerja: " + idRekin);
        }

        Map<String, Object> pembuat = pegawaiService.getMappedPegawai(Crypto.decrypt(reqDTO.getNip_pembuat()));
        String nipPembuat = (String) pembuat.get("nip");
        String namaPembuat = (String) pembuat.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipPembuat))
                .nama(namaPembuat)
                .build();

        int tingkatRisiko = reqDTO.getSkala_dampak() * reqDTO.getSkala_kemungkinan();

        String levelRisiko =
                (tingkatRisiko >= 1 && tingkatRisiko <= 4)  ? "Rendah" :
                        (tingkatRisiko >= 5 && tingkatRisiko <= 12) ? "Menengah" :
                                (tingkatRisiko >= 15 && tingkatRisiko <= 25) ? "Tinggi" : "-";

        Analisa analisa = Analisa.builder()
                .idRencanaKinerja(idRekin)
                .namaRisiko(reqDTO.getNama_risiko())
                .penyebab(reqDTO.getPenyebab())
                .akibat(reqDTO.getAkibat())
                .skalaDampak(reqDTO.getSkala_dampak())
                .skalaKemungkinan(reqDTO.getSkala_kemungkinan())
                .tingkatRisiko(tingkatRisiko)
                .levelRisiko(levelRisiko)
                .status(StatusEnum.PENDING)
                .pembuat(pegawai)
                .verifikator(null)
                .build();

        Analisa saved = analisaRepository.save(analisa);

        return buildTOFFromRkAndAnalisa(rkNode, saved);
    }

    @Override
    @Transactional
    public AnalisaResDTO updateAnalisa(String idRekin, AnalisaReqDTO reqDTO) {
        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");
        JsonNode rkNode = objectMapper.convertValue(rekinDetail.get("rencana_kinerja"), JsonNode.class);

        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        Analisa analisa = analisaRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for ID Rencana Kinerja: " + idRekin));

        Map<String, Object> checkIdRekinUpdated = rencanaKinerjaService.getDetailRencanaKinerja(reqDTO.getId_rencana_kinerja());
        Object checkIdRekinUpdatedObj = checkIdRekinUpdated.get("rencana_kinerja");

        if (checkIdRekinUpdatedObj instanceof Map == false) {
            throw new ResourceNotFoundException("YOUR UPDATED: ID Rencana Kinerja " + reqDTO.getId_rencana_kinerja() + " is not valid");
        }

        Map<String, Object> pembuat = pegawaiService.getMappedPegawai(Crypto.decrypt(reqDTO.getNip_pembuat()));
        String nipPembuat = (String) pembuat.get("nip");
        String namaPembuat = (String) pembuat.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipPembuat))
                .nama(namaPembuat)
                .build();

        int tingkatRisiko = reqDTO.getSkala_dampak() * reqDTO.getSkala_kemungkinan();

        String levelRisiko = (tingkatRisiko >= 1 && tingkatRisiko <= 4) ? "Rendah" :
                (tingkatRisiko >= 5 && tingkatRisiko <= 12) ? "Menengah" :
                        (tingkatRisiko >= 15 && tingkatRisiko <= 25) ? "Tinggi" : "-";

        analisa.setIdRencanaKinerja(reqDTO.getId_rencana_kinerja());
        analisa.setNamaRisiko(reqDTO.getNama_risiko());
        analisa.setPenyebab(reqDTO.getPenyebab());
        analisa.setAkibat(reqDTO.getAkibat());
        analisa.setSkalaDampak(reqDTO.getSkala_dampak());
        analisa.setSkalaKemungkinan(reqDTO.getSkala_kemungkinan());
        analisa.setTingkatRisiko(tingkatRisiko);
        analisa.setLevelRisiko(levelRisiko);
        analisa.setPembuat(pegawai);

        Analisa updated = analisaRepository.save(analisa);

        return buildTOFFromRkAndAnalisa(rkNode, updated);
    }

    @Override
    @Transactional
    public AnalisaResDTO verifyAnalisa(String idRekin, AnalisaReqDTO.UpdateStatusDTO updateDTO) {
        Analisa entity = analisaRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for ID Rencana Kinerja: " + idRekin));

        Map<String, Object> verifikator = pegawaiService.getMappedPegawai(Crypto.decrypt(updateDTO.getNip_verifikator()));
        String nipVerifikator = (String) verifikator.get("nip");
        String namaVerifikator = (String) verifikator.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipVerifikator))
                .nama(namaVerifikator)
                .build();

        try {
            StatusEnum status = StatusEnum.valueOf(updateDTO.getStatus());
            entity.setStatus(status);
            entity.setKeterangan(updateDTO.getKeterangan());
            entity.setVerifikator(pegawai);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status value: " + updateDTO.getStatus());
        }

        Analisa updated = analisaRepository.save(entity);

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Rencana kinerja not found for id_rencana_kinerja: " + idRekin);
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        return buildTOFFromRkAndAnalisa(rkNode, updated);
    }

    @Override
    @Transactional
    public void deleteAnalisa(String idRekin) {
        Analisa analisa = analisaRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Identifikasi not found for id_rencana_kinerja: " + idRekin));

        analisaRepository.delete(analisa);
    }
}
