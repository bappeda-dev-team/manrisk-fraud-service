package cc.kertaskerja.manrisk_fraud.service.risikoKecurangan;

import cc.kertaskerja.manrisk_fraud.dto.risikoKecurangan.RisikoKecuranganDTO;
import cc.kertaskerja.manrisk_fraud.entity.RisikoKecurangan;
import cc.kertaskerja.manrisk_fraud.exception.ResourceNotFoundException;
import cc.kertaskerja.manrisk_fraud.repository.RisikoKecuranganRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RisikoKecuranganServiceImpl implements RisikoKecuranganService {

    private final RisikoKecuranganRepository risikoKecuranganRepository;

    private RisikoKecuranganDTO toDTO(RisikoKecurangan risikoKecurangan) {
        return RisikoKecuranganDTO.builder()
                .jenis_risiko(risikoKecurangan.getJenisRisiko())
                .uraian(risikoKecurangan.getUraian())
                .build();
    }

    @Override
    public List<RisikoKecuranganDTO> findAllByJenisRisiko(String jenisRisiko) {
        if (jenisRisiko != null) {
            List<RisikoKecurangan> uraian = risikoKecuranganRepository.findByJenisRisikoFlexible(jenisRisiko);
            if (uraian == null || uraian.isEmpty()) {
                throw new ResourceNotFoundException("Data uraian dengan jenis risiko '" + jenisRisiko + "' tidak ditemukan");
            }

            return uraian.stream().map(this::toDTO).toList();
        } else {
            List<RisikoKecurangan> risiko = risikoKecuranganRepository.findAll();

            return risiko.stream()
                    .map(RisikoKecurangan::getJenisRisiko)
                    .distinct()
                    .map(jenisRisikoValue -> {
                        RisikoKecuranganDTO dto = new RisikoKecuranganDTO();
                        dto.setJenis_risiko(jenisRisikoValue);
                        dto.setUraian(null);
                        return dto;
                    })
                    .toList();
        }
    }

    @Override
    @Transactional
    public RisikoKecuranganDTO save(RisikoKecuranganDTO dto) {
        RisikoKecurangan risikoKecurangan = RisikoKecurangan.builder()
                .jenisRisiko(dto.getJenis_risiko() != null ? dto.getJenis_risiko().toUpperCase() : null)
                .uraian(dto.getUraian())
                .build();

        RisikoKecurangan saved = risikoKecuranganRepository.save(risikoKecurangan);

        return toDTO(saved);
    }

    @Override
    @Transactional
    public RisikoKecuranganDTO update(Long id, RisikoKecuranganDTO dto) {
        RisikoKecurangan risikoKecurangan = risikoKecuranganRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data Risiko Kecurangan tidak ditemukan"));

        risikoKecurangan.setJenisRisiko(dto.getJenis_risiko() != null ? dto.getJenis_risiko().toUpperCase() : null);
        risikoKecurangan.setUraian(dto.getUraian());

        RisikoKecurangan updated = risikoKecuranganRepository.save(risikoKecurangan);

        return toDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RisikoKecurangan risikoKecurangan = risikoKecuranganRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Data Risiko Kecurangan tidak ditemukan"));

        risikoKecuranganRepository.delete(risikoKecurangan);
    }
}


