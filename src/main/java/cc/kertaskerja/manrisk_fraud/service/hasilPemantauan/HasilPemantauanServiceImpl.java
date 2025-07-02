package cc.kertaskerja.manrisk_fraud.service.hasilPemantauan;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.dto.hasilPemantauan.HasilPemantauanReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.hasilPemantauan.HasilPemantauanResDTO;
import cc.kertaskerja.manrisk_fraud.entity.HasilPemantauan;
import cc.kertaskerja.manrisk_fraud.entity.Pemantauan;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.helper.Crypto;
import cc.kertaskerja.manrisk_fraud.repository.HasilPemantauanRepository;
import cc.kertaskerja.manrisk_fraud.repository.PemantauanRepository;
import cc.kertaskerja.manrisk_fraud.service.global.PegawaiService;
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
    private final PegawaiService pegawaiService;
    private final PemantauanRepository pemantauanRepository;
    private final HasilPemantauanRepository hpRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Hibernate6Module())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    private HasilPemantauanResDTO buildDTOFFromHasilPemantauan(JsonNode rk, JsonNode pemantauan, HasilPemantauan h) {
        return HasilPemantauanResDTO.builder()
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(HasilPemantauanResDTO.OperasionalDaerah.builder()
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
                .status(h.getStatus() != null ? h.getStatus().name() : null)
                .pembuat(h.getPembuat())
                .verifikator(h.getVerifikator())
                .keterangan(h.getKeterangan())
                .build();
    }

    private HasilPemantauanResDTO buildDTOFFromRkOnly(JsonNode rk, JsonNode pemantauan) {
        return HasilPemantauanResDTO.builder()
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(HasilPemantauanResDTO.OperasionalDaerah.builder()
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
    public List<HasilPemantauanResDTO> findAll(String nip, String tahun) {
        if (!Crypto.isEncrypted(nip)) {
            throw new ResourceNotFoundException("NIP is not encrypted: " + Crypto.decrypt(nip));
        }

        Map<String, Object> rekin = rencanaKinerjaService.getRencanaKinerja(Crypto.decrypt(nip), tahun);
        Object rkObj = rekin.get("rencana_kinerja");

        if (!(rkObj instanceof List)) {
            throw new ResourceNotFoundException("No 'Rencana Kinerja' data found for NIP: " + Crypto.decrypt(nip) + " and year: " + tahun);
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

        List<HasilPemantauanResDTO> result = new ArrayList<>();

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
        }

        return result;
    }

    @Override
    public HasilPemantauanResDTO findOne(String idRekin) {
        // Get external RK data
        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found for id_rencana_kinerja: " + idRekin);
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        // Find Pemantauan
        Pemantauan pemantauan = pemantauanRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data pemantauan not found for id_rencana_kinerja: " + idRekin));
        JsonNode pemantauanNode = objectMapper.convertValue(pemantauan, JsonNode.class);

        // Find HasilPemantauan
        HasilPemantauan hasilPemantauan = hpRepository.findById(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data hasil pemantauan not found for id_rencana_kinerja: " + idRekin));

        // Build DTO
        return buildDTOFFromHasilPemantauan(rkNode, pemantauanNode, hasilPemantauan);
    }

    @Override
    @Transactional
    public HasilPemantauanResDTO save(HasilPemantauanReqDTO dto) {
        String idRekin = dto.getId_rencana_kinerja();

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("id_rencana_kinerja is required");
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

        Map<String, Object> pembuat = pegawaiService.getMappedPembuat(Crypto.decrypt(dto.getNip_pembuat()));
        String nipPembuat = (String) pembuat.get("nip");
        String namaPembuat = (String) pembuat.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipPembuat))
                .nama(namaPembuat)
                .build();

        int tingkatRisiko = (dto.getSkala_kemungkinan() * dto.getSkala_dampak());
        String levelRisiko =
                (tingkatRisiko >= 1 && tingkatRisiko <= 4)  ? "Rendah" :
                        (tingkatRisiko >= 5 && tingkatRisiko <= 12) ? "Menengah" :
                                (tingkatRisiko >= 15 && tingkatRisiko <= 25) ? "Tinggi" : "-";

        HasilPemantauan hasilPemantauan = HasilPemantauan.builder()
                .skalaDampak(dto.getSkala_dampak())
                .skalaKemungkinan(dto.getSkala_kemungkinan())
                .tingkatRisiko(tingkatRisiko)
                .levelRisiko(levelRisiko)
                .pemantauan(pemantauan)
                .status(StatusEnum.PENDING)
                .pembuat(pegawai)
                .build();

        HasilPemantauan saved = hpRepository.save(hasilPemantauan);

        return buildDTOFFromHasilPemantauan(rkNode, pemantauanNode, saved);
    }

    @Override
    @Transactional
    public HasilPemantauanResDTO update(String idRekin, HasilPemantauanReqDTO dto) {
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

        HasilPemantauan hp = hpRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data hasil pemantauan not found for id_rencana_kinerja: " + idRekin));

        Map<String, Object> checkIdRekinUpdated = rencanaKinerjaService.getDetailRencanaKinerja(dto.getId_rencana_kinerja());
        Object checkIdRekinUpdatedObj = checkIdRekinUpdated.get("rencana_kinerja");

        if (checkIdRekinUpdatedObj instanceof Map == false) {
            throw new ResourceNotFoundException("YOUR UPDATED: ID Rencana Kinerja " + dto.getId_rencana_kinerja() + " is not valid");
        }

        Map<String, Object> pembuat = pegawaiService.getMappedPembuat(Crypto.decrypt(dto.getNip_pembuat()));
        String nipPembuat = (String) pembuat.get("nip");
        String namaPembuat = (String) pembuat.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipPembuat))
                .nama(namaPembuat)
                .build();

        int tingkatRisiko = (dto.getSkala_kemungkinan() * dto.getSkala_dampak());
        String levelRisiko = tingkatRisiko <= 4 ? "Rendah"
                : tingkatRisiko <= 12 ? "Menengah"
                : tingkatRisiko <= 25 ? "Tinggi"
                : "-";

        hp.setSkalaDampak(dto.getSkala_dampak());
        hp.setSkalaKemungkinan(dto.getSkala_kemungkinan());
        hp.setTingkatRisiko(tingkatRisiko);
        hp.setLevelRisiko(levelRisiko);
        hp.setPemantauan(pemantauan);
        hp.setPembuat(pegawai);

        HasilPemantauan saved = hpRepository.save(hp);

        return buildDTOFFromHasilPemantauan(rkNode, pemantauanNode, saved);
    }

    @Override
    @Transactional
    public HasilPemantauanResDTO verify(String idRekin, HasilPemantauanReqDTO.UpdateStatusDTO updateDTO) {
        HasilPemantauan hp = hpRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data Hasil Pemantauan not found for id_rencana_kinerja: " + idRekin));

        Map<String, Object> verifikator = pegawaiService.getMappedPembuat(Crypto.decrypt(updateDTO.getNip_verifikator()));
        String nipVerifikator = (String) verifikator.get("nip");
        String namaVerifikator = (String) verifikator.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipVerifikator))
                .nama(namaVerifikator)
                .build();

        try {
            StatusEnum status = StatusEnum.valueOf(updateDTO.getStatus());
            hp.setStatus(status);
            hp.setVerifikator(pegawai);
            hp.setKeterangan(updateDTO.getKeterangan());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid status: " + updateDTO.getStatus());
        }

        HasilPemantauan updated = hpRepository.save(hp);

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Rencana kinerja not found for id_rencana_kinerja: " + idRekin);
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        Pemantauan pemantauan = pemantauanRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for id_rencana_kinerja: " + idRekin));
        JsonNode pemantauanNode = objectMapper.convertValue(pemantauan, JsonNode.class);

        if (hpRepository.existsByIdRencanaKinerja(idRekin)) {
            throw new ResourceNotFoundException("Data hasil pemantauan already exists for id_rencana_kinerja: " + idRekin);
        }

        return buildDTOFFromHasilPemantauan(rkNode, pemantauanNode, updated);
    }

    @Override
    @Transactional
    public void delete(String idRekin) {
        HasilPemantauan hp = hpRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data hasil pemantauan not found for id_rencana_kinerja: " + idRekin));

        hpRepository.delete(hp);
    }
}
