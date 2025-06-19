package cc.kertaskerja.manrisk_fraud.service.penangangan;

import cc.kertaskerja.manrisk_fraud.dto.PenangananDTO;
import cc.kertaskerja.manrisk_fraud.entity.Penangangan;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.repository.PenangananRepository;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PenangananServiceImpl implements PenangananService {

    private final PenangananRepository penangananRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PenangananServiceImpl(PenangananRepository penangananRepository, RencanaKinerjaService rencanaKinerjaService) {
        this.penangananRepository = penangananRepository;
        this.rencanaKinerjaService = rencanaKinerjaService;
    }

    private PenangananDTO buildDTOFromRkAndPenanganan(JsonNode rk, Penangangan penanganan) {
        return PenangananDTO.builder()
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
                .operasional_daerah(PenangananDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .existing_control(penanganan.getExistingControl())
                .jenis_perlakuan_risiko(penanganan.getJenisPerlakuanRisiko())
                .rencana_perlakuan_risiko(penanganan.getRencanaPerlakuanRisiko())
                .biaya_perlakuan_risiko(penanganan.getBiayaPerlakuanRisiko())
                .target_waktu(penanganan.getTargetWaktu())
                .pic(penanganan.getPic())
                .status(penanganan.getStatus())
                .keterangan(penanganan.getKeterangan())
                .created_at(penanganan.getCreatedAt())
                .updated_at(penanganan.getUpdatedAt())
                .build();
    }

    private PenangananDTO buildDTOFromRkOnly(JsonNode rk) {
        return PenangananDTO.builder()
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
                .operasional_daerah(PenangananDTO.OperasionalDaerah.builder()
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
    public List<PenangananDTO> findAllPenanganan(String nip, String tahun) {
        Map<String, Object> externalResponse = rencanaKinerjaService.getRencanaKinerja(nip, tahun);
        Object rkObj = externalResponse.get("rencana_kinerja");

        if (!(rkObj instanceof List)) {
            throw new ResourceNotFoundException("No 'Rencana Kinerja' data found for NIP: " + nip + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkObj;
        List<Penangangan> penangananList = penangananRepository.findAll();

        Map<String, List<Penangangan>> penangananMap = new HashMap<>();
        for (int i = 0; i < penangananList.size(); i++) {
            Penangangan p = penangananList.get(i);
            String idRekin = p.getIdRekin();

            if (!penangananMap.containsKey(idRekin)) {
                penangananMap.put(idRekin, new ArrayList<>());
            }

            penangananMap.get(idRekin).add(p);
        }

        List<PenangananDTO> result = new ArrayList<>();

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
    public PenangananDTO findOnePenanganan(String idRekin) {
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
    public PenangananDTO savePenanganan(PenangananDTO penangananDTO) {
        String idRekin = penangananDTO.getId_rencana_kinerja();

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("id_rencana_kinerja is required");
        }

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        if (penangananRepository.existsByIdRekin(idRekin)) {
            throw new ResourceNotFoundException("Data penanganan already exists for id_rencana_kinerja: " + idRekin);
        }

        Penangangan penanganan = Penangangan.builder()
                .idRekin(idRekin)
                .existingControl(penangananDTO.getExisting_control())
                .jenisPerlakuanRisiko(penangananDTO.getJenis_perlakuan_risiko())
                .rencanaPerlakuanRisiko(penangananDTO.getRencana_perlakuan_risiko())
                .biayaPerlakuanRisiko(penangananDTO.getBiaya_perlakuan_risiko())
                .targetWaktu(penangananDTO.getTarget_waktu())
                .pic(penangananDTO.getPic())
                .status(penangananDTO.getStatus())
                .keterangan(penangananDTO.getKeterangan())
                .build();


        Penangangan saved = penangananRepository.save(penanganan);

        return buildDTOFromRkAndPenanganan(rkNode, saved) ;
    }

    @Override
    @Transactional
    public PenangananDTO updatePenanganan(String idRekin, PenangananDTO penangananDTO) {
        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        Penangangan penangangan = penangananRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data penanganan not found for id_rencana_kinerja: " + idRekin));

        penangangan.setExistingControl(penangananDTO.getExisting_control());
        penangangan.setJenisPerlakuanRisiko(penangananDTO.getJenis_perlakuan_risiko());
        penangangan.setRencanaPerlakuanRisiko(penangananDTO.getRencana_perlakuan_risiko());
        penangangan.setBiayaPerlakuanRisiko(penangananDTO.getBiaya_perlakuan_risiko());
        penangangan.setTargetWaktu(penangananDTO.getTarget_waktu());
        penangangan.setPic(penangananDTO.getPic());
        penangangan.setStatus(penangananDTO.getStatus());
        penangangan.setKeterangan(penangananDTO.getKeterangan());

        Penangangan updated = penangananRepository.save(penangangan);
        JsonNode rkNode = objectMapper.convertValue(rekinDetail, JsonNode.class);

        return buildDTOFromRkAndPenanganan(rkNode, updated);
    }

    @Override
    @Transactional
    public PenangananDTO updateStatusPenanganan(String idRekin, PenangananDTO.UpdateStatusDTO updateDTO) {
        Penangangan entity = penangananRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data penanganan not found for id_rencana_kinerja: " + idRekin));

        entity.setStatus(updateDTO.getStatus());
        entity.setKeterangan(updateDTO.getKeterangan());

        Penangangan updated = penangananRepository.save(entity);

        // Optional: get rencana_kinerja data if you want to populate full DTO
        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (rkObj instanceof Map == false) {
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
