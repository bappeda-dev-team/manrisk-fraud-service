package cc.kertaskerja.manrisk_fraud.service.identifikasi;

import cc.kertaskerja.manrisk_fraud.dto.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import cc.kertaskerja.manrisk_fraud.exception.InternalServerException;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.repository.IdentifikasiRepository;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    private IdentifikasiDTO buildDTOFromRkAndIdentifikasi(JsonNode rk, Identifikasi ident) {
        return IdentifikasiDTO.builder()
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
                .operasional_daerah(IdentifikasiDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .nama_risiko(ident.getNamaRisiko())
                .jenis_risiko(ident.getJenisRisiko())
                .kemungkinan_kecurangan(ident.getKemungkinanKecurangan())
                .indikasi(ident.getIndikasi())
                .kemungkinan_pihak_terkait(ident.getKemungkinanPihakTerkait())
                .status(ident.getStatus())
                .keterangan(ident.getKeterangan())
                .created_at(ident.getCreatedAt())
                .updated_at(ident.getUpdatedAt())
                .build();
    }

    private IdentifikasiDTO buildDTOFromRkOnly(JsonNode rk) {
        return IdentifikasiDTO.builder()
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
                .operasional_daerah(IdentifikasiDTO.OperasionalDaerah.builder()
                        .kode_opd(rk.path("operasional_daerah").path("kode_opd").asText())
                        .nama_opd(rk.path("operasional_daerah").path("nama_opd").asText())
                        .build())
                .nama_risiko("")
                .jenis_risiko("")
                .kemungkinan_kecurangan("")
                .indikasi("")
                .kemungkinan_pihak_terkait("")
                .status("")
                .keterangan("")
                .created_at(null)
                .updated_at(null)
                .build();
    }


    @Override
    public List<IdentifikasiDTO> findAllIdentifikasi(String nip, String tahun) {
        // Step 1: Call external API via RencanaKinerjaService
        Map<String, Object> eksternalResponse = rencanaKinerjaService.getRencanaKinerja(nip, tahun);
        Object rkObj = eksternalResponse.get("rencana_kinerja");

        if (!(rkObj instanceof List<?> rkList)) {
            throw new ResourceNotFoundException("No 'Rencana Kinerja' data found for NIP: " + nip + " and year: " + tahun);
        }

        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) rkList;

        // Step 2: Get local data
        List<Identifikasi> identifikasiList = identifikasiRepository.findAll();
        Map<String, List<Identifikasi>> identifikasiMap = identifikasiList.stream()
                .collect(Collectors.groupingBy(Identifikasi::getIdRekin));

        // Step 3: Combine both
        List<IdentifikasiDTO> result = new ArrayList<>();

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
    public IdentifikasiDTO findOneIdentifikasi(String idRekin) {
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
    public IdentifikasiDTO saveIdentifikasi(IdentifikasiDTO identifikasiDTO) {
        String idRekin = identifikasiDTO.getId_rencana_kinerja();

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
        if (rkObj == null) {
            throw new ResourceNotFoundException("Data rencana_kinerja not found in external API");
        }

        JsonNode rkNode = objectMapper.convertValue(rkObj, JsonNode.class);

        // Step 2: Check for existing record
        if (identifikasiRepository.existsByIdRekin(idRekin)) {
            throw new InternalServerException("Data identifikasi already exists for id_rencana_kinerja: " + idRekin);
        }

        // Step 3: Save entity
        Identifikasi ident = Identifikasi.builder()
                .idRekin(idRekin)
                .namaRisiko(identifikasiDTO.getNama_risiko())
                .jenisRisiko(identifikasiDTO.getJenis_risiko())
                .kemungkinanKecurangan(identifikasiDTO.getKemungkinan_kecurangan())
                .indikasi(identifikasiDTO.getIndikasi())
                .kemungkinanPihakTerkait(identifikasiDTO.getKemungkinan_pihak_terkait())
                .status("Pending")
                .keterangan(identifikasiDTO.getKeterangan())
                .build();

        Identifikasi saved = identifikasiRepository.save(ident);

        // Step 4: Combine and return
        return buildDTOFromRkAndIdentifikasi(rkNode, saved);
    }

    @Override
    @Transactional
    public IdentifikasiDTO updateIdentifikasi(String idRekin, IdentifikasiDTO identifikasiDTO) {
        Identifikasi ident = identifikasiRepository.findOneByIdRekin(idRekin)
                .orElseThrow(() -> new ResourceNotFoundException("Data identifikasi not found for ID Rencana Kinerja: " + idRekin));

        // Update fields
        ident.setNamaRisiko(identifikasiDTO.getNama_risiko());
        ident.setJenisRisiko(identifikasiDTO.getJenis_risiko());
        ident.setKemungkinanKecurangan(identifikasiDTO.getKemungkinan_kecurangan());
        ident.setIndikasi(identifikasiDTO.getIndikasi());
        ident.setKemungkinanPihakTerkait(identifikasiDTO.getKemungkinan_pihak_terkait());
        ident.setKeterangan(identifikasiDTO.getKeterangan());

        Identifikasi updated = identifikasiRepository.save(ident);

        // Get external data for response
        Map<String, Object> detailResponse = rencanaKinerjaService.getDetailRencanaKinerja(idRekin);
        JsonNode rkNode = objectMapper.convertValue(detailResponse.get("rencana_kinerja"), JsonNode.class);

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
