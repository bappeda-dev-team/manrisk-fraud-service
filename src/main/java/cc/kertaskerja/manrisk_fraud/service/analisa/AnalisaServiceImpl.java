package cc.kertaskerja.manrisk_fraud.service.analisa;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaResDTO;
import cc.kertaskerja.manrisk_fraud.entity.Analisa;
import cc.kertaskerja.manrisk_fraud.entity.TujuanEx;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.exception.BadRequestException;
import cc.kertaskerja.manrisk_fraud.exception.InternalServerException;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.helper.Crypto;
import cc.kertaskerja.manrisk_fraud.repository.AnalisaRepository;
import cc.kertaskerja.manrisk_fraud.repository.TujuanExRepository;
import cc.kertaskerja.manrisk_fraud.service.external.penetapan.PenetapanService;
import cc.kertaskerja.manrisk_fraud.service.external.rekin.RencanaKinerjaService;
import cc.kertaskerja.manrisk_fraud.service.global.PegawaiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalisaServiceImpl implements AnalisaService {

    private final AnalisaRepository analisaRepository;
    private final TujuanExRepository tujuanExRepository;
    private final RencanaKinerjaService rekinService;
    private final PenetapanService penetapanService;
    private final PegawaiService pegawaiService;
    private final ObjectMapper objectMapper;

    // --- HELPER METHODS FOR MAPPING ---

    private AnalisaResDTO buildTOFFromRkAndAnalisa(JsonNode rk, Analisa analisa, JsonNode tujuanNode) {

        AnalisaResDTO.RencanaKinerjaDTO rencanaKinerjaDTO = AnalisaResDTO.RencanaKinerjaDTO.builder()
              .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText(null))
              .id_pohon(rk.path("id_pohon").asInt())
              .nama_pohon(rk.path("nama_pohon").asText(null))
              .level_pohon(rk.path("level_pohon").asInt())
              .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText(null))
              .tahun(rk.path("tahun").asText(null))
              .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText(null))
              .pegawai_id(rk.path("pegawai_id").asText(null))
              .nama_pegawai(rk.path("nama_pegawai").asText(null))
              .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText(null))
              .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText(null))
              .build();

        AnalisaResDTO.TujuanOpdDTO tujuanOpdDTO = null;

        if (analisa.getTujuan() != null && tujuanNode != null && !tujuanNode.isNull() && !tujuanNode.isMissingNode()) {
            String kodeOpd = analisa.getTujuan().getKodeTujuanOpd();
            List<AnalisaResDTO.IndikatorDTO> indikatorList = new ArrayList<>();

            if (tujuanNode.has("indikators") && tujuanNode.get("indikators").isArray()) {
                for (JsonNode indNode : tujuanNode.get("indikators")) {
                    List<AnalisaResDTO.TargetDTO> targetList = new ArrayList<>();

                    if (indNode.has("targets") && indNode.get("targets").isArray()) {
                        for (JsonNode tgtNode : indNode.get("targets")) {
                            targetList.add(AnalisaResDTO.TargetDTO.builder()
                                  .id(tgtNode.path("id").asLong())
                                  .kode_target(tgtNode.path("kode_target").asText(null))
                                  .tahun(tgtNode.path("tahun").asInt())
                                  .target(tgtNode.path("target").asInt())
                                  .satuan(tgtNode.path("satuan").asText(null))
                                  .build());
                        }
                    }

                    indikatorList.add(AnalisaResDTO.IndikatorDTO.builder()
                          .id(indNode.path("id").asLong())
                          .kode_indikator(indNode.path("kode_indikator").asText(null))
                          .indikator(indNode.path("indikator").asText(null))
                          .rumus_perhitungan(indNode.path("rumus_perhitungan").asText(null))
                          .sumber_data(indNode.path("sumber_data").asText(null))
                          .definisi_operasional(indNode.path("definisi_operasional").asText(null))
                          .tahun_aktif(indNode.path("tahun_aktif").asInt())
                          .targets(targetList)
                          .build());
                }
            }

            tujuanOpdDTO = AnalisaResDTO.TujuanOpdDTO.builder()
                  .kode_tujuan_opd(tujuanNode.path("kode_tujuan_opd").asText(kodeOpd))
                  .tujuan_opd(tujuanNode.path("tujuan_opd").asText(null))
                  .periode(tujuanNode.path("periode").asText(null))
                  .indikators(indikatorList.isEmpty() ? null : indikatorList)
                  .build();
        }

        return AnalisaResDTO.builder()
              .id(analisa.getId())
              .tujuan_opd(tujuanOpdDTO)
              .rencana_kinerja(rencanaKinerjaDTO)
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
        AnalisaResDTO.RencanaKinerjaDTO rencanaKinerjaDTO = AnalisaResDTO.RencanaKinerjaDTO.builder()
              .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
              .id_pohon(rk.path("id_pohon").asInt())
              .nama_pohon(rk.path("nama_pohon").asText())
              .level_pohon(rk.path("level_pohon").asInt())
              .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
              .tahun(rk.path("tahun").asText())
              .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
              .pegawai_id(rk.path("pegawai_id").asText())
              .nama_pegawai(rk.path("nama_pegawai").asText())
              .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
              .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
              .build();

        return AnalisaResDTO.builder()
              .id(null)
              .tujuan_opd(null) // Otomatis hilang dari JSON
              .rencana_kinerja(rencanaKinerjaDTO)
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

    private AnalisaResDTO.TujuanOpdDTO buildTujuanOpdDTO(
          JsonNode tujuanNode,
          String kodeTujuanOpd
    ) {

        List<AnalisaResDTO.IndikatorDTO> indikatorList = new ArrayList<>();

        if (tujuanNode.has("indikators") && tujuanNode.get("indikators").isArray()) {
            for (JsonNode indNode : tujuanNode.get("indikators")) {
                List<AnalisaResDTO.TargetDTO> targetList = new ArrayList<>();
                if (indNode.has("targets") && indNode.get("targets").isArray()) {
                    for (JsonNode tgtNode : indNode.get("targets")) {
                        targetList.add(
                              AnalisaResDTO.TargetDTO.builder()
                                    .id(tgtNode.path("id").asLong())
                                    .kode_target(tgtNode.path("kode_target").asText(null))
                                    .tahun(tgtNode.path("tahun").asInt())
                                    .target(tgtNode.path("target").asInt())
                                    .satuan(tgtNode.path("satuan").asText(null))
                                    .build()
                        );
                    }
                }

                indikatorList.add(
                      AnalisaResDTO.IndikatorDTO.builder()
                            .id(indNode.path("id").asLong())
                            .kode_indikator(indNode.path("kode_indikator").asText(null))
                            .indikator(indNode.path("indikator").asText(null))
                            .rumus_perhitungan(indNode.path("rumus_perhitungan").asText(null))
                            .sumber_data(indNode.path("sumber_data").asText(null))
                            .definisi_operasional(indNode.path("definisi_operasional").asText(null))
                            .tahun_aktif(indNode.path("tahun_aktif").asInt())
                            .targets(targetList)
                            .build()
                );
            }
        }

        return AnalisaResDTO.TujuanOpdDTO.builder()
              .kode_tujuan_opd(tujuanNode.path("kode_tujuan_opd").asText(kodeTujuanOpd))
              .tujuan_opd(tujuanNode.path("tujuan_opd").asText(null))
              .periode(tujuanNode.path("periode").asText(null))
              .indikators(indikatorList.isEmpty() ? null : indikatorList)
              .build();
    }

    private JsonNode getTujuanNode(String kodeOpd, String tahun, String kodeTujuanOpd, Map<String, List<Map<String, Object>>> cache) {
        if (kodeOpd == null || kodeOpd.isEmpty() || kodeTujuanOpd == null || tahun == null) return null;

        List<Map<String, Object>> tujuanList;
        String cacheKey = kodeOpd + "_" + tahun;

        if (cache != null) {
            tujuanList = cache.computeIfAbsent(cacheKey, k -> penetapanService.findAllTujuanOpd(kodeOpd, tahun));
        } else {
            tujuanList = penetapanService.findAllTujuanOpd(kodeOpd, tahun);
        }

        if (tujuanList == null || tujuanList.isEmpty()) {
            return null;
        }

        Map<String, Object> targetTujuan = tujuanList.stream()
              .filter(t -> kodeTujuanOpd.equals(t.get("kode_tujuan_opd")))
              .findFirst()
              .orElse(null);

        return targetTujuan != null ? objectMapper.convertValue(targetTujuan, JsonNode.class) : null;
    }

    // --- MAIN SERVICE METHODS ---

    @Override
    public List<AnalisaResDTO> findAllAnalisa(String nip, String tahun) {
        if (!Crypto.isEncrypted(nip)) {
            throw new ResourceNotFoundException("NIP is not encrypted: " + nip);
        }

        Map<String, Object> externalResponse = rekinService.getRencanaKinerja(Crypto.decrypt(nip), tahun);
        Object rkObj = externalResponse.get("rencana_kinerja");

        if (!(rkObj instanceof List<?> rkList)) {
            throw new ResourceNotFoundException("No 'rencana_kinerja' data found for NIP: " + Crypto.decrypt(nip) + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkList;
        List<Analisa> analisaList = analisaRepository.findAll();

        Map<String, List<Analisa>> analisaMap = analisaList.stream()
              .filter(a -> a.getIdRencanaKinerja() != null)
              .collect(Collectors.groupingBy(a -> a.getIdRencanaKinerja().trim()));

        List<AnalisaResDTO> result = new ArrayList<>();

        for (Map<String, Object> rk : rekinList) {
            JsonNode rkNode = objectMapper.convertValue(rk, JsonNode.class);
            String idRencana = rkNode.path("id_rencana_kinerja").asText("").trim();

            List<Analisa> temp = analisaMap.getOrDefault(idRencana, List.of());

            if (!temp.isEmpty()) {
                for (Analisa analisa : temp) {
                    result.add(buildTOFFromRkAndAnalisa(rkNode, analisa, null));
                }
            } else {
                result.add(buildTOFFromRkOnly(rkNode));
            }
        }

        return result;
    }

    @Override
    public AnalisaResDTO.AnalisaByTujuanResDTO findAllByTujuanOpd(
          String kodeTujuanOpd,
          Integer tahun
    ) {

        TujuanEx tujuanEx = tujuanExRepository.findByKodeTujuanOpdAndTahun(kodeTujuanOpd, tahun).orElseThrow(() ->
                    new ResourceNotFoundException(
                          "Tujuan OPD tidak ditemukan: "
                                + kodeTujuanOpd
                    ));

        List<Analisa> analisaList = analisaRepository.findAllByKodeTujuanOpdAndTahun(kodeTujuanOpd, tahun);

        if (analisaList.isEmpty()) {
            throw new ResourceNotFoundException("Data analisa tidak ditemukan");
        }

        Analisa firstAnalisa = analisaList.get(0);

        Map<String, Object> rekinDetail = rekinService.getDetailRencanaKinerja(firstAnalisa.getIdRencanaKinerja());

        JsonNode rkNode = objectMapper.convertValue(rekinDetail.get("rencana_kinerja"), JsonNode.class);

        String kodeOpd = rkNode
              .path("operasional_daerah")
              .path("kode_opd")
              .asText();

        JsonNode tujuanNode = getTujuanNode(kodeOpd, String.valueOf(tahun), kodeTujuanOpd, null);

        if (tujuanNode == null) {
            throw new ResourceNotFoundException("Tujuan OPD tidak ditemukan pada API Penetapan");
        }

        List<AnalisaResDTO> analisaDTOs = new ArrayList<>();

        for (Analisa analisa : analisaList) {
            Map<String, Object> detail = rekinService.getDetailRencanaKinerja(analisa.getIdRencanaKinerja());
            JsonNode rk = objectMapper.convertValue(detail.get("rencana_kinerja"), JsonNode.class);
            AnalisaResDTO dto = buildTOFFromRkAndAnalisa(rk, analisa, null);
            dto.setTujuan_opd(null);
            analisaDTOs.add(dto);
        }

        AnalisaResDTO.TujuanOpdDTO tujuanDTO = buildTujuanOpdDTO(tujuanNode, kodeTujuanOpd);

        return AnalisaResDTO.AnalisaByTujuanResDTO.builder()
              .kode_tujuan_opd(tujuanDTO.getKode_tujuan_opd())
              .tujuan_opd(tujuanDTO.getTujuan_opd())
              .periode(tujuanDTO.getPeriode())
              .indikators(tujuanDTO.getIndikators())
              .analisa(analisaDTOs)
              .build();
    }

    @Override
    public AnalisaResDTO findOneAnalisa(String idRekin) {
        Map<String, Object> detailResponse = rekinService.getDetailRencanaKinerja(idRekin);
        Object rkObj = detailResponse.get("rencana_kinerja");

        if (rkObj == null) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        return analisaRepository.findOneByIdRekin(idRekin)
              .map(a -> {
                  JsonNode tujuanNode = null;
                  if (a.getTujuan() != null) {
                      String kodeOpd = rkNode.path("operasional_daerah").path("kode_opd").asText();
                      String tahunTujuan = String.valueOf(a.getTujuan().getTahun());
                      tujuanNode = getTujuanNode(kodeOpd, tahunTujuan, a.getTujuan().getKodeTujuanOpd(), null);
                  }
                  return buildTOFFromRkAndAnalisa(rkNode, a, tujuanNode);
              })
              .orElseGet(() -> buildTOFFromRkOnly(rkNode));
    }

    @Override
    @Transactional
    public AnalisaResDTO saveAnalisa(AnalisaReqDTO reqDTO) {
        String kodeTujuanOpd = reqDTO.getKodeTujuanOpd();
        Integer tahun = reqDTO.getTahun();
        String idRekin = reqDTO.getId_rencana_kinerja();

        if (kodeTujuanOpd == null || tahun == null) {
            throw new ResourceNotFoundException("Kode Tujuan OPD atau Tahun tidak boleh kosong");
        }

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("ID rekin tidak boleh kosong");
        }

        Map<String, Object> rekinDetail = rekinService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);
        String kodeOpd = rkNode.path("operasional_daerah").path("kode_opd").asText();

        if (analisaRepository.existsByIdRencanaKinerja(idRekin)) {
            throw new InternalServerException("Data identifikasi already exists for id_rencana_kinerja: " + idRekin);
        }

        JsonNode tujuanNode = getTujuanNode(kodeOpd, String.valueOf(tahun), kodeTujuanOpd, null);
        if (tujuanNode == null) {
            throw new ResourceNotFoundException("Tujuan OPD dengan kode " + kodeTujuanOpd + " tidak ditemukan di API Penetapan");
        }

        TujuanEx tujuanEx = tujuanExRepository.findByKodeTujuanOpdAndTahun(kodeTujuanOpd, tahun)
              .orElseGet(() -> {
                  TujuanEx newTujuan = TujuanEx.builder()
                        .kodeTujuanOpd(kodeTujuanOpd)
                        .tahun(tahun)
                        .build();
                  return tujuanExRepository.save(newTujuan);
              });

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

        Analisa analisa = Analisa.builder()
              .tujuan(tujuanEx)
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

        return buildTOFFromRkAndAnalisa(rkNode, saved, tujuanNode);
    }

    @Override
    @Transactional
    public AnalisaResDTO updateAnalisa(String idRekin, AnalisaReqDTO reqDTO) {
        Map<String, Object> rekinDetail = rekinService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");
        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        Analisa analisa = analisaRepository.findOneByIdRekin(idRekin)
              .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for ID Rencana Kinerja: " + idRekin));

        Map<String, Object> checkIdRekinUpdated = rekinService.getDetailRencanaKinerja(reqDTO.getId_rencana_kinerja());
        Object checkIdRekinUpdatedObj = checkIdRekinUpdated.get("rencana_kinerja");

        if (!(checkIdRekinUpdatedObj instanceof Map)) {
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

        JsonNode tujuanNode = null;
        if (updated.getTujuan() != null) {
            String kodeOpd = rkNode.path("operasional_daerah").path("kode_opd").asText();
            String tahunTujuan = String.valueOf(updated.getTujuan().getTahun());
            tujuanNode = getTujuanNode(kodeOpd, tahunTujuan, updated.getTujuan().getKodeTujuanOpd(), null);
        }

        return buildTOFFromRkAndAnalisa(rkNode, updated, tujuanNode);
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

        Map<String, Object> rekinDetail = rekinService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Rencana kinerja not found for id_rencana_kinerja: " + idRekin);
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        JsonNode tujuanNode = null;
        if (updated.getTujuan() != null) {
            String kodeOpd = rkNode.path("operasional_daerah").path("kode_opd").asText();
            String tahunTujuan = String.valueOf(updated.getTujuan().getTahun());
            tujuanNode = getTujuanNode(kodeOpd, tahunTujuan, updated.getTujuan().getKodeTujuanOpd(), null);
        }

        return buildTOFFromRkAndAnalisa(rkNode, updated, tujuanNode);
    }

    @Override
    @Transactional
    public void deleteAnalisa(String idRekin) {
        Analisa analisa = analisaRepository.findOneByIdRekin(idRekin)
              .orElseThrow(() -> new ResourceNotFoundException("Identifikasi not found for id_rencana_kinerja: " + idRekin));

        analisaRepository.delete(analisa);
    }
}