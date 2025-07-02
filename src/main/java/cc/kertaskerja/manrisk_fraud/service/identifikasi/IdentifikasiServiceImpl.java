package cc.kertaskerja.manrisk_fraud.service.identifikasi;

import cc.kertaskerja.manrisk_fraud.dto.PegawaiInfo;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiResDTO;
import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import cc.kertaskerja.manrisk_fraud.enums.StatusEnum;
import cc.kertaskerja.manrisk_fraud.exception.InternalServerException;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.helper.Crypto;
import cc.kertaskerja.manrisk_fraud.repository.IdentifikasiRepository;
import cc.kertaskerja.manrisk_fraud.service.global.PegawaiService;
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
public class IdentifikasiServiceImpl implements IdentifikasiService {

    private final IdentifikasiRepository identifikasiRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final PegawaiService pegawaiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private IdentifikasiResDTO buildDTOFromRkAndIdentifikasi(JsonNode rk, Identifikasi ident) {
        return IdentifikasiResDTO.builder()
                .id(ident.getId())
                .id_rencana_kinerja(rk.path("id_rencana_kinerja").asText())
                .id_pohon(rk.path("id_pohon").asInt())
                .nama_pohon(rk.path("nama_pohon").asText())
                .level_pohon(rk.path("level_pohon").asInt())
                .nama_rencana_kinerja(rk.path("nama_rencana_kinerja").asText())
                .tahun(rk.path("tahun").asText())
                .status_rencana_kinerja(rk.path("status_rencana_kinerja").asText())
                .pegawai_id(rk.path("pegawai_id").asText())
                .nama_pegawai(rk.path("nama_pegawai").asText())
                .operasional_daerah(IdentifikasiResDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .nama_risiko(ident.getNamaRisiko())
                .jenis_risiko(ident.getJenisRisiko())
                .uraian(ident.getUraian())
                .kemungkinan_kecurangan(ident.getKemungkinanKecurangan())
                .indikasi(ident.getIndikasi())
                .kemungkinan_pihak_terkait(ident.getKemungkinanPihakTerkait())
                .status(ident.getStatus() != null ? ident.getStatus().name() : null)
                .pembuat(ident.getPembuat())
                .verifikator(ident.getVerifikator())
                .keterangan(ident.getKeterangan())
                .build();
    }

    private IdentifikasiResDTO buildDTOFromRkOnly(JsonNode rk) {
        return IdentifikasiResDTO.builder()
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
                .operasional_daerah(IdentifikasiResDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .nama_risiko("")
                .jenis_risiko("")
                .uraian("")
                .kemungkinan_kecurangan("")
                .indikasi("")
                .kemungkinan_pihak_terkait("")
                .status("")
                .keterangan("")
                .build();
    }

    @Override
    public List<IdentifikasiResDTO> findAllIdentifikasi(String nip, String tahun) {
        if (!Crypto.isEncrypted(nip)) {
            throw new ResourceNotFoundException("NIP is not encrypted: " + nip);
        }

        // Step 1: Call external API via RencanaKinerjaService
        Map<String, Object> eksternalResponse = rencanaKinerjaService.getRencanaKinerja(Crypto.decrypt(nip), tahun);
        Object rkObj = eksternalResponse.get("rencana_kinerja");

        if (!(rkObj instanceof List<?> rkList)) {
            throw new ResourceNotFoundException("No 'Rencana Kinerja' data found for NIP: " + Crypto.decrypt(nip) + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkList;

        // Step 2: Get local data
        List<Identifikasi> identifikasiList = identifikasiRepository.findAll();
        Map<String, List<Identifikasi>> identifikasiMap = identifikasiList.stream()
                .collect(Collectors.groupingBy(Identifikasi::getIdRencanaKinerja));

        // Step 3: Combine both
        List<IdentifikasiResDTO> result = new ArrayList<>();

        for (Map<String, Object> rk : rekinList) {
            JsonNode rkNode = objectMapper.convertValue(rk, JsonNode.class);
            String idRencana = rkNode.get("id_rencana_kinerja").asText();
            List<Identifikasi> temp = identifikasiMap.getOrDefault(idRencana, List.of());

            if (!temp.isEmpty()) {
                for (Identifikasi ident : temp) {
                    result.add(buildDTOFromRkAndIdentifikasi(rkNode, ident));
                }
            } else {
                result.add(buildDTOFromRkOnly(rkNode));
            }
        }

        return result;
    }

    @Override
    public IdentifikasiResDTO findOneIdentifikasi(String idRekin) {
        // Step 1: Ambil data dari external API
        Map<String, Object> detailResponse = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = detailResponse.get("rencana_kinerja");

        if (rkObj == null) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        // Step 2: Cari di database lokal
        return identifikasiRepository.findOneByIdRekin(idRekin)
                .map(ident -> buildDTOFromRkAndIdentifikasi(rkNode, ident)) // Jika ada, gabungkan data lokal + eksternal
                .orElseGet(() -> buildDTOFromRkOnly(rkNode)); // Jika tidak ada, tampilkan data dari eksternal saja
    }

    @Override
    @Transactional
    public IdentifikasiResDTO saveIdentifikasi(IdentifikasiReqDTO reqDTO) {
        String idRekin = reqDTO.getId_rencana_kinerja();

        if (idRekin == null || idRekin.isEmpty()) {
            throw new ResourceNotFoundException("id_rencana_kinerja is required");
        }

        // Step 1: Validate against external API via the RencanaKinerjaService
        Map<String, Object> detailResponse;
        try {
            detailResponse = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        } catch (Exception e) {
            throw new ResourceNotFoundException("ID Rencana Kinerja not found in external API: " + idRekin);
        }

        Object rkObj = detailResponse.get("rencana_kinerja");
        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        // Step 2: Check for existing record
        if (identifikasiRepository.existsByIdRencanaKinerja(idRekin)) {
            throw new InternalServerException("Data identifikasi already exists for id_rencana_kinerja: " + idRekin);
        }

        Map<String, Object> pembuat = pegawaiService.getMappedPegawai(Crypto.decrypt(reqDTO.getNip_pembuat()));
        String nipPembuat = (String) pembuat.get("nip");
        String namaPembuat = (String) pembuat.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipPembuat))
                .nama(namaPembuat)
                .build();

        // Step 3: Save entity
        Identifikasi ident = Identifikasi.builder()
                .idRencanaKinerja(idRekin)
                .namaRisiko(reqDTO.getNama_risiko())
                .jenisRisiko(reqDTO.getJenis_risiko())
                .uraian(reqDTO.getUraian())
                .kemungkinanKecurangan(reqDTO.getKemungkinan_kecurangan())
                .indikasi(reqDTO.getIndikasi())
                .kemungkinanPihakTerkait(reqDTO.getKemungkinan_pihak_terkait())
                .status(StatusEnum.PENDING)
                .pembuat(pegawai)
                .build();

        Identifikasi saved = identifikasiRepository.save(ident);

        // Step 4: Combine and return
        return buildDTOFromRkAndIdentifikasi(rkNode, saved);
    }

    @Override
    @Transactional
    public IdentifikasiResDTO updateIdentifikasi(String idRekin, IdentifikasiReqDTO reqDTO) {
        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (rkObj instanceof Map == false) {
            throw new ResourceNotFoundException("Data rencana kinerja is not found");
        }

        Identifikasi ident = identifikasiRepository.findOneByIdRekin(idRekin)
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

        // Update fields
        ident.setIdRencanaKinerja(reqDTO.getId_rencana_kinerja());
        ident.setNamaRisiko(reqDTO.getNama_risiko());
        ident.setJenisRisiko(reqDTO.getJenis_risiko());
        ident.setUraian(reqDTO.getUraian());
        ident.setKemungkinanKecurangan(reqDTO.getKemungkinan_kecurangan());
        ident.setIndikasi(reqDTO.getIndikasi());
        ident.setKemungkinanPihakTerkait(reqDTO.getKemungkinan_pihak_terkait());
        ident.setPembuat(pegawai);

        Identifikasi updated = identifikasiRepository.save(ident);

        // Get external data for response
        Map<String, Object> detailResponse = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        JsonNode rkNode = objectMapper.convertValue(detailResponse.get("rencana_kinerja"), JsonNode.class);

        return buildDTOFromRkAndIdentifikasi(rkNode, updated);
    }

    @Override
    @Transactional
    public IdentifikasiResDTO verifyIdentifikasi(String idRekin, IdentifikasiReqDTO.UpdateStatusDTO updateDTO) {
        Identifikasi identifikasi = identifikasiRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Identifikasi not found for id_rencana_kinerja: " + idRekin));

        Map<String, Object> verifikator = pegawaiService.getMappedPegawai(Crypto.decrypt(updateDTO.getNip_verifikator()));
        String nipVerifikator = (String) verifikator.get("nip");
        String namaVerifikator = (String) verifikator.get("nama");
        PegawaiInfo pegawai = PegawaiInfo.builder()
                .nip(Crypto.encrypt(nipVerifikator))
                .nama(namaVerifikator)
                .build();

        try {
            StatusEnum status = StatusEnum.valueOf(updateDTO.getStatus());
            identifikasi.setStatus(status);
            identifikasi.setKeterangan(updateDTO.getKeterangan());
            identifikasi.setVerifikator(pegawai);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid status value: " + updateDTO.getStatus());
        }

        Identifikasi updated = identifikasiRepository.save(identifikasi);

        Map<String, Object> rekinDetail = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        Object rkObj = rekinDetail.get("rencana_kinerja");

        if (!(rkObj instanceof Map)) {
            throw new ResourceNotFoundException("Rencana kinerja not found for id_rencana_kinerja: " + idRekin);
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        return buildDTOFromRkAndIdentifikasi(rkNode, updated);
    }

    @Override
    @Transactional
    public void deleteIdentifikasi(String idRekin) {
        Identifikasi identifikasi = identifikasiRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Identifikasi not found for id_rencana_kinerja: " + idRekin));

        identifikasiRepository.delete(identifikasi);
    }
}
