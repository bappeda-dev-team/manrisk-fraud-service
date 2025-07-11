package cc.kertaskerja.manrisk_fraud.service.penangangan;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.dto.penanganan.PenangananReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.penanganan.PenangananResDTO;
import cc.kertaskerja.manrisk_fraud.entity.Penangangan;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.helper.Crypto;
import cc.kertaskerja.manrisk_fraud.repository.PenangananRepository;
import cc.kertaskerja.manrisk_fraud.service.global.PegawaiService;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PenangananServiceImpl implements PenangananService {

    private final PenangananRepository penangananRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final PegawaiService pegawaiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PenangananServiceImpl(PenangananRepository penangananRepository,
                                 RencanaKinerjaService rencanaKinerjaService,
                                 PegawaiService pegawaiService) {
        this.penangananRepository = penangananRepository;
        this.rencanaKinerjaService = rencanaKinerjaService;
        this.pegawaiService = pegawaiService;
    }

    private PenangananResDTO buildDTOFromRkAndPenanganan(JsonNode rk, Penangangan penanganan) {
        return PenangananResDTO.builder()
                .id(penanganan.getId())
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(PenangananResDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .existing_control(penanganan.getExistingControl())
                .jenis_perlakuan_risiko(penanganan.getJenisPerlakuanRisiko())
                .rencana_perlakuan_risiko(penanganan.getRencanaPerlakuanRisiko())
                .biaya_perlakuan_risiko(penanganan.getBiayaPerlakuanRisiko())
                .target_waktu(penanganan.getTargetWaktu())
                .pic(penanganan.getPic())
                .status(penanganan.getStatus() != null ? penanganan.getStatus().name() : null)
                .keterangan(penanganan.getKeterangan())
                .pembuat(penanganan.getPembuat())
                .verifikator(penanganan.getVerifikator())
                .keterangan(penanganan.getKeterangan())
                .build();
    }

    private PenangananResDTO buildDTOFromRkOnly(JsonNode rk) {
        return PenangananResDTO.builder()
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
                .operasional_daerah(PenangananResDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .existing_control("")
                .jenis_perlakuan_risiko("")
                .rencana_perlakuan_risiko("")
                .biaya_perlakuan_risiko("")
                .target_waktu("")
                .pic("")
                .status("")
                .keterangan("")
                .created_at(null)
                .updated_at(null)
                .build();
    }


    @Override
    public List<PenangananResDTO> findAllPenanganan(String nip, String tahun) {
        if (!Crypto.isEncrypted(nip)) {
            throw new ResourceNotFoundException("NIP is not encrypted: " + nip);
        }

        Map<String, Object> externalResponse = rencanaKinerjaService.getRencanaKinerja(Crypto.decrypt(nip), tahun);
        Object rkObj = externalResponse.get("rencana_kinerja");

        if (!(rkObj instanceof List)) {
            throw new ResourceNotFoundException("No 'Rencana Kinerja' data found for NIP: " + Crypto.decrypt(nip) + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkObj;
        List<Penangangan> penangananList = penangananRepository.findAll();

        Map<String, List<Penangangan>> penangananMap = new HashMap<>();
        for (int i = 0; i < penangananList.size(); i++) {
            Penangangan p = penangananList.get(i);
            String idRekin = p.getIdRencanaKinerja();

            if (!penangananMap.containsKey(idRekin)) {
                penangananMap.put(idRekin, new ArrayList<>());
            }

            penangananMap.get(idRekin).add(p);
        }

        List<PenangananResDTO> result = new ArrayList<>();

        for (int i = 0; i < rekinList.size(); i++) {
            Map<String, Object> rk = rekinList.get(i);
            JsonNode rkNode = objectMapper.convertValue(rk, JsonNode.class);
            String idRekin = rkNode.get("id_rencana_kinerja").asText();

            List<Penangangan> temp = penangananMap.get(idRekin);
            if (temp != null && temp.size() > 0) {
                for (int j = 0; j < temp.size(); j++) {
                    Penangangan p = temp.get(j);
                    result.add(buildDTOFromRkAndPenanganan(rkNode, p));
                }
            } else {
                result.add(buildDTOFromRkOnly(rkNode));
            }
        }

        return result;
    }

    @Override
    public PenangananResDTO findOnePenanganan(String idRekin) {
        Map<String, Object> detailData = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = detailData.get("rencana_kinerja");

        if (rkObj == null) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        return penangananRepository.findOneByIdRekin(idRekin)
                .map(p -> buildDTOFromRkAndPenanganan(rkNode, p))
                .orElseGet(() -> buildDTOFromRkOnly(rkNode)) ;
    }

    @Override
    @Transactional
    public PenangananResDTO savePenanganan(PenangananReqDTO reqDTO) {
        String idRekin = reqDTO.getId_rencana_kinerja();

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("id_rencana_kinerja is required");
        }

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        if (penangananRepository.existsByIdRencanaKinerja(idRekin)) {
            throw new ResourceNotFoundException("Data penanganan already exists for id_rencana_kinerja: " + idRekin);
        }

        Map<String, Object> pembuat = pegawaiService.getMappedPegawai(Crypto.decrypt(reqDTO.getNip_pembuat()));
        String nipPembuat = (String) pembuat.get("nip");
        String namaPembuat = (String) pembuat.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipPembuat))
                .nama(namaPembuat)
                .build();

        Penangangan penanganan = Penangangan.builder()
                .idRencanaKinerja(idRekin)
                .existingControl(reqDTO.getExisting_control())
                .jenisPerlakuanRisiko(reqDTO.getJenis_perlakuan_risiko())
                .rencanaPerlakuanRisiko(reqDTO.getRencana_perlakuan_risiko())
                .biayaPerlakuanRisiko(reqDTO.getBiaya_perlakuan_risiko())
                .targetWaktu(reqDTO.getTarget_waktu())
                .pic(reqDTO.getPic())
                .status(StatusEnum.PENDING)
                .pembuat(pegawai)
                .build();

        Penangangan saved = penangananRepository.save(penanganan);

        return buildDTOFromRkAndPenanganan(rkNode, saved) ;
    }

    @Override
    @Transactional
    public PenangananResDTO updatePenanganan(String idRekin, PenangananReqDTO reqDTO) {
        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");
        JsonNode rkNode = objectMapper.convertValue(rekinDetail.get("rencana_kinerja"), JsonNode.class);

        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        Penangangan penangangan = penangananRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data penanganan not found for id_rencana_kinerja: " + idRekin));

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

        penangangan.setExistingControl(reqDTO.getExisting_control());
        penangangan.setJenisPerlakuanRisiko(reqDTO.getJenis_perlakuan_risiko());
        penangangan.setRencanaPerlakuanRisiko(reqDTO.getRencana_perlakuan_risiko());
        penangangan.setBiayaPerlakuanRisiko(reqDTO.getBiaya_perlakuan_risiko());
        penangangan.setTargetWaktu(reqDTO.getTarget_waktu());
        penangangan.setPic(reqDTO.getPic());
        penangangan.setPembuat(pegawai);

        Penangangan updated = penangananRepository.save(penangangan);

        return buildDTOFromRkAndPenanganan(rkNode, updated);
    }

    @Override
    @Transactional
    public PenangananResDTO verifyPenanganan(String idRekin, PenangananReqDTO.UpdateStatusDTO updateDTO) {
        Penangangan entity = penangananRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data penanganan not found for id_rencana_kinerja: " + idRekin));

        Map<String, Object> verifikator = pegawaiService.getMappedPegawai(Crypto.decrypt(updateDTO.getNip_verifikator()));
        String nipVerifikator = (String) verifikator.get("nip");
        String namaVerifikator = (String) verifikator.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipVerifikator))
                .nama(namaVerifikator)
                .build();

        try {
            StatusEnum statusEnum = StatusEnum.valueOf(updateDTO.getStatus().toUpperCase());
            entity.setStatus(statusEnum);
            entity.setVerifikator(pegawai);
            entity.setKeterangan(updateDTO.getKeterangan());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Status tidak valid: " + updateDTO.getStatus());
        }

        Penangangan updated = penangananRepository.save(entity);

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Rencana kinerja not found for id_rencana_kinerja: " + idRekin);
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        return buildDTOFromRkAndPenanganan(rkNode, updated);
    }

    @Override
    @Transactional
    public void deletePenanganan(String idRekin) {
        Penangangan penanganan = penangananRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data penanganan not found for id_rencana_kinerja: " + idRekin));

        penangananRepository.delete(penanganan);
    }
}
