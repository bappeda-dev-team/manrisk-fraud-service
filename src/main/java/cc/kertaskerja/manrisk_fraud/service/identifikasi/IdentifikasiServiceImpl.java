package cc.kertaskerja.manrisk_fraud.service.identifikasi;

import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import cc.kertaskerja.manrisk_fraud.entity.Manrisk;
import cc.kertaskerja.manrisk_fraud.repository.IdentifikasiRepository;
import cc.kertaskerja.manrisk_fraud.repository.ManriskRepository;
import cc.kertaskerja.manrisk_fraud.service.global.RencanaKinerjaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

                if (map.get("operasional_daerah") instanceof Map<?, ?> opdMap) {
                    String kodeOpd = (String) opdMap.get("kode_opd");
                    String namaOpd = (String) opdMap.get("nama_opd");
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
        List<Map<String, Object>> rekinList = (List<Map<String, Object>>) externalData.get("rencana_kinerja");

        return identifikasiList.stream()
                .map(identifikasi -> {
                    String idManrisk = identifikasi.getIdManrisk().getIdManrisk();
                    Map<String, Object> matchingRekin = rekinList.stream()
                            .filter(r -> idManrisk.equals(r.get("id_rencana_kinerja")))
                            .findFirst()
                            .orElse(null);

                    if (matchingRekin != null) {
                        return toDTO(identifikasi, Map.of("rencana_kinerja", List.of(matchingRekin)));
                    }
                    return null;
                })
                .filter(dto -> dto != null) // Filter out nulls (non-matches)
                .collect(Collectors.toList());
    }

    @Override
    public IdentifikasiDTO findOneIdentifikasi(String idManrisk) {
        Identifikasi identifikasi = identifikasiRepository
                .findByIdManrisk(idManrisk)
                .orElseThrow(() -> new RuntimeException("Identifikasi not found for ID Manrisk: " + idManrisk));

        Map<String, Object> rekin = rekinService.getDetailRencanaKinerja(idManrisk);
        Object rk = rekin.get("rencana_kinerja");

        if (rk instanceof Map<?, ?> rencanaMap) {
            return toDTO(identifikasi, Map.of("rencana_kinerja", List.of(rencanaMap)));
        } else {
            return toDTO(identifikasi, null);
        }
    }

    @Override
    @Transactional
    public IdentifikasiDTO saveDataIdentifikasi(IdentifikasiDTO identifikasiDto) {
        // Check if id_manrisk already exists
        boolean exists = manriskRepository.existsById(identifikasiDto.getId_manrisk());

        if (exists) {
            throw new RuntimeException("Data identifikasi dengan id_manrisk " + identifikasiDto.getId_manrisk() + " sudah ada.");
        }

        // Save to table manrisk first
        Manrisk newManrisk = new Manrisk();
        newManrisk.setIdManrisk(identifikasiDto.getId_manrisk());
        manriskRepository.save(newManrisk);

        // Create identifikasi entity manually (not using builder)
        Identifikasi identifikasi = new Identifikasi();
        identifikasi.setNamaRisiko(identifikasiDto.getNama_risiko());
        identifikasi.setJenisRisiko(identifikasiDto.getJenis_risiko());
        identifikasi.setKemungkinanKecurangan(identifikasiDto.getKemungkinan_kecurangan());
        identifikasi.setIndikasi(identifikasiDto.getIndikasi());
        identifikasi.setKemungkinanPihakTerkait(identifikasiDto.getKemungkinan_pihak_terkait());
        identifikasi.setKeterangan(identifikasiDto.getKeterangan());
        identifikasi.setIdManrisk(newManrisk);

        // Debug before saving
        System.out.println("Before save - namaRisiko: " + identifikasi.getNamaRisiko());
        System.out.println("Before save - jenisRisiko: " + identifikasi.getJenisRisiko());

        Identifikasi saved = identifikasiRepository.save(identifikasi);
        return toPostDTO(saved);
    }
}
