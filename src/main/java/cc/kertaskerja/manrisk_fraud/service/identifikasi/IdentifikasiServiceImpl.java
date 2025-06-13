package cc.kertaskerja.manrisk_fraud.service.identifikasi;

import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import cc.kertaskerja.manrisk_fraud.entity.Manrisk;
import cc.kertaskerja.manrisk_fraud.repository.IdentifikasiRepository;
import cc.kertaskerja.manrisk_fraud.repository.ManriskRepository;
import cc.kertaskerja.manrisk_fraud.service.global.AccessTokenService;
import cc.kertaskerja.manrisk_fraud.service.global.RedisTokenService;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdentifikasiServiceImpl implements IdentifikasiService {

    private final ManriskRepository manriskRepository;
    private final IdentifikasiRepository identifikasiRepository;
    private final RencanaKinerjaService rekinService;

    private IdentifikasiDTO toDTO(Identifikasi identifikasi, Map<String, Object> externalData) {
        IdentifikasiDTO dto = new IdentifikasiDTO(
                identifikasi.getId(),
                identifikasi.getNamaRisiko(),
                identifikasi.getJenisRisiko(),
                identifikasi.getKemungkinanKecurangan(),
                identifikasi.getIndikasi(),
                identifikasi.getKemungkinanPihakTerkait(),
                identifikasi.getKeterangan(),
                null, // id_pohon
                null, // nama_pohon
                null, // nama_rencana_kinerja
                null, // tahun
                null, // status_rencana_kinerja
                null, // operasional_daerah
                null, // id_pegawai
                null, // nama_pegawai,
                identifikasi.getIdManrisk().getIdManrisk(),
                identifikasi.getCreatedAt(),
                identifikasi.getUpdatedAt()
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

                if (map.get("operasional_daerah") instanceof Map<?, ?> daerahMap) {
                    String kodeOpd = (String) daerahMap.get("kode_opd");
                    String namaOpd = (String) daerahMap.get("nama_opd");
                    dto.setOperasional_daerah(new IdentifikasiDTO.OperasionalDaerah(kodeOpd, namaOpd));
                }
            }
        }

        return dto;
    }

    private IdentifikasiDTO toPostDTO(Identifikasi identifikasi) {
        return new IdentifikasiDTO(
                identifikasi.getId(),
                identifikasi.getNamaRisiko(),
                identifikasi.getJenisRisiko(),
                identifikasi.getKemungkinanKecurangan(),
                identifikasi.getIndikasi(),
                identifikasi.getKemungkinanPihakTerkait(),
                identifikasi.getKeterangan(),
                null, null, null, null, null, null, null, null,
                identifikasi.getIdManrisk().getIdManrisk(),
                identifikasi.getCreatedAt(),
                null
        );
    }

    @Override
    public List<IdentifikasiDTO> findAllIdentifikasi(String nip, String tahun) {
        List<Identifikasi> identifikasiList = identifikasiRepository.findAll();

        Map<String, Object> externalData = rekinService.getRencanaKinerja(nip, tahun);
        List<Map<String, Object>> rencanaList = (List<Map<String, Object>>) externalData.get("rencana_kinerja");

        return identifikasiList.stream()
                .map(identifikasi -> {
                    String idManrisk = identifikasi.getIdManrisk().getIdManrisk();
                    Map<String, Object> matchingRencana = rencanaList.stream()
                            .filter(r -> idManrisk.equals(r.get("id_rencana_kinerja")))
                            .findFirst()
                            .orElse(null);

                    if (matchingRencana != null) {
                        return toDTO(identifikasi, Map.of("rencana_kinerja", List.of(matchingRencana)));
                    }
                    return null;
                })
                .filter(dto -> dto != null) // Filter out nulls (non-matches)
                .collect(Collectors.toList());
    }

    @Override
    public IdentifikasiDTO findOneIdentifikasi(String idManrisk) {
        Identifikasi identifikasi = identifikasiRepository
                .findByIdManrisk_IdManrisk(idManrisk)
                .orElseThrow(() -> new RuntimeException("Identifikasi not found for idManrisk: " + idManrisk));

        // Panggil API eksternal
        Map<String, Object> detailResponse = rekinService.getDetailRencanaKinerja(idManrisk);
        Object rk = detailResponse.get("rencana_kinerja");

        if (rk instanceof Map<?, ?> rencanaMap) {
            // Bungkus menjadi list agar toDTO mengolahnya
            return toDTO(identifikasi, Map.of("rencana_kinerja", List.of(rencanaMap)));
        } else {
            // fallback tanpa data rencana_kinerja
            return toDTO(identifikasi, null);
        }
    }

    @Override
    @Transactional
    public IdentifikasiDTO saveDataIdentifikasi(IdentifikasiDTO identifikasiDto) {
        // Check if id_manrisk already exists
        boolean exists = manriskRepository.existsById(identifikasiDto.getId_manrisk());
        System.out.println("IDENTIFIKASI DTO + " + identifikasiDto.getNama_risiko());
        if (exists) {
            throw new RuntimeException("Data identifikasi dengan id_manrisk " + identifikasiDto.getId_manrisk() + " sudah ada.");
        }

        // Save to table manrisk first
        Manrisk newManrisk = Manrisk.builder()
                .idManrisk(identifikasiDto.getId_manrisk())
                .build();
        manriskRepository.save(newManrisk);

        // Save to table identifikasi - FIX: Use the correct getter methods
        Identifikasi identifikasi = Identifikasi.builder()
                .namaRisiko(identifikasiDto.getNama_risiko())           // Fixed: was using wrong getter
                .jenisRisiko(identifikasiDto.getJenis_risiko())         // Fixed: was using wrong getter
                .kemungkinanKecurangan(identifikasiDto.getKemungkinan_kecurangan()) // Fixed: was using wrong getter
                .indikasi(identifikasiDto.getIndikasi())                // This was correct
                .kemungkinanPihakTerkait(identifikasiDto.getKemungkinan_pihak_terkait()) // Fixed: was using wrong getter
                .keterangan(identifikasiDto.getKeterangan())            // This was correct
                .idManrisk(newManrisk)                                  // This was correct
                .build();

        Identifikasi saved = identifikasiRepository.save(identifikasi);
        return toPostDTO(saved);
    }
}
