package cc.kertaskerja.manrisk_fraud.service.identifikasi;

import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import cc.kertaskerja.manrisk_fraud.repository.IdentifikasiRepository;
import cc.kertaskerja.manrisk_fraud.service.global.AccessTokenService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdentifikasiServiceImpl implements IdentifikasiService {

    private final IdentifikasiRepository identifikasiRepository;
    private final RestTemplate restTemplate;
    private final AccessTokenService accessTokenService;


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
                .nama_risiko("-")
                .jenis_risiko("-")
                .kemungkinan_kecurangan("-")
                .indikasi("-")
                .kemungkinan_pihak_terkait("-")
                .status("-")
                .keterangan("-")
                .created_at(null)
                .updated_at(null)
                .build();
    }


    @Override
    public List<IdentifikasiDTO> findAllIdentifikasi(String nip, String tahun) {
        String token = accessTokenService.getAccessToken();
        String url = String.format("https://api-ekak.zeabur.app/get_rencana_kinerja/pegawai/%s?tahun=%s", nip, tahun);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        JsonNode eksternalResponse = response.getBody();

        List<JsonNode> rencanaKinerjaList = new ArrayList<>();
        if (eksternalResponse != null && eksternalResponse.has("rencana_kinerja")) {
            eksternalResponse.get("rencana_kinerja").forEach(rencanaKinerjaList::add);
        }

        List<Identifikasi> identifikasiList = identifikasiRepository.findAll();

        Map<String, List<Identifikasi>> identifikasiMap = identifikasiList.stream()
                .collect(Collectors.groupingBy(Identifikasi::getIdRekin));

        List<IdentifikasiDTO> result = new ArrayList<>();

        for (JsonNode rk : rencanaKinerjaList) {
            String idRencana = rk.get("id_rencana_kinerja").asText();
            List<Identifikasi> terkait = identifikasiMap.getOrDefault(idRencana, List.of());

            if (!terkait.isEmpty()) {
                for (Identifikasi ident : terkait) {
                    result.add(buildDTOFromRkAndIdentifikasi(rk, ident));
                }
            } else {
                result.add(buildDTOFromRkOnly(rk));
            }
        }

        return result;
    }

    @Override
    public IdentifikasiDTO findOneIdentifikasi(String idManrisk) {
        return null;
    }

    @Override
    public IdentifikasiDTO saveIdentifikasi(IdentifikasiDTO identifikasiDTO) {
        return null;
    }
}
