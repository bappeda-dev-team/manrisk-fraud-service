package cc.kertaskerja.manrisk_fraud.service.analisa;

import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaDTO;
import cc.kertaskerja.manrisk_fraud.entity.Analisa;
import cc.kertaskerja.manrisk_fraud.repository.AnalisaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalisaServiceImpl implements AnalisaService {

    private final AnalisaRepository analisaRepository;

    private AnalisaDTO toDTO(Analisa analisa) {
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

        return new AnalisaDTO(
                analisa.getId(),
                analisa.getNamaRisiko(),
                analisa.getPenyebab(),
                analisa.getAkibat(),
                analisa.getSkalaDampak(),
                analisa.getSkalaKemungkinan(),
                analisa.getTingkatRisiko(),
                status,
                analisa.getKeterangan(),
                analisa.getCreatedAt(),
                analisa.getUpdatedAt()
        );
    }


    @Override
    public List<AnalisaDTO> findAllAnalisa() {
        return analisaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
