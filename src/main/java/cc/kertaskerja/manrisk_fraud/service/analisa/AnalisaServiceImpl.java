package cc.kertaskerja.manrisk_fraud.service.analisa;

import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaDTO;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.entity.Analisa;
import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import cc.kertaskerja.manrisk_fraud.repository.AnalisaRepository;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalisaServiceImpl implements AnalisaService {

    private final AnalisaRepository analisaRepository;
    private final RencanaKinerjaService rekinService;

    private AnalisaDTO toDTO(Analisa analisa, Map<String, Object> externalData) {
        int score = analisa.getSkalaDampak() * analisa.getSkalaKemungkinan();

        String status;
        if (score >= 1 && score <= 4) {
            status = "Rendah";
        } else if (score >= 5 && score <= 12) {
            status = "Menengah";
        } else if (score >= 15 && score <= 25) {
            status = "Tinggi";
        } else {
            status = "-";
        }

        AnalisaDTO dto = new AnalisaDTO(
                analisa.getId(),
                analisa.getNamaRisiko(),
                analisa.getPenyebab(),
                analisa.getAkibat(),
                analisa.getSkalaDampak(),
                analisa.getSkalaKemungkinan(),
                score,
                status,
                analisa.getStatus(),
                analisa.getKeterangan(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                analisa.getIdManrisk().getIdManrisk(),
                analisa.getCreatedAt(),
                analisa.getUpdatedAt()
        );

        if (externalData != null && externalData.get("rencana_kinerja") instanceof List<?> rencanaList) {
            if (!rencanaList.isEmpty() && rencanaList.get(0) instanceof Map<?, ?> map) {
                dto.setId_pohon((Integer) map.get("id_pohon"));
                dto.setNama_pohon((String) map.get("nama_pohon"));
                dto.setNama_rencana_kinerja((String) map.get("nama_rencana_kinerja"));
                dto.setTahun((String) map.get("tahun"));
                dto.setStatus_rencana_kinerja((String) map.get("status_rencana_kinerja"));
                dto.setId_pegawai((String) map.get("pegawai_id"));
                dto.setNama_pegawai((String) map.get("nama_pegawai"));

                if (map.get("operasional_daerah") instanceof Map<?, ?> opdMap) {
                    String kodeOpd = (String) opdMap.get("kode_opd");
                    String namaOpd = (String) opdMap.get("nama_opd");
                    dto.setOperasional_daerah(new AnalisaDTO.OperasionalDaerah(kodeOpd, namaOpd));
                }
            }
        }

        return dto;
    }


    @Override
    public List<AnalisaDTO> findAllAnalisa(String nip, String tahun) {
        List<Analisa> analisaList = analisaRepository.findAll();

        Map<String, Object> externalData = rekinService.getRencanaKinerja(nip, tahun);
        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) externalData.get("rencana_kinerja");

        return analisaList.stream()
                .map(analisa -> {
                    String idManrisk = analisa.getIdManrisk().getIdManrisk();
                    Map<String, Object> matchingRekin = rekinList.stream()
                            .filter(r -> idManrisk.equals(r.get("id_rencana_kinerja")))
                            .findFirst()
                            .orElse(null);

                    if (matchingRekin != null) {
                        return toDTO(analisa, Map.of("rencana_kinerja", List.of(matchingRekin)));
                    }
                    return null;
                })
                .filter(dto -> dto != null) // Filter out nulls (non-matches)
                .collect(Collectors.toList());
    }

    @Override
    public AnalisaDTO findOneAnalisa(String idManrisk) {
        Analisa analisa = analisaRepository
                .findByIdManrisk(idManrisk)
                .orElseThrow(() -> new RuntimeException("Analisa not found for ID Manrisk: " + idManrisk));

        Map<String, Object> rekin = rekinService.getDetailRencanaKinerja(idManrisk);
        Object rk = rekin.get("rencana_kinerja");

        if (rk instanceof Map<?, ?> rencanaMap) {
            return toDTO(analisa, Map.of("rencana_kinerja", List.of(rencanaMap)));
        } else {
            return toDTO(analisa, null);
        }
    }
}
