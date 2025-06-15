package cc.kertaskerja.manrisk_fraud.service.analisa;

import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalisaServiceImpl implements AnalisaService {
    @Override
    public List<AnalisaDTO> findAllAnalisa(String nip, String tahun) {
        return List.of();
    }

    @Override
    public AnalisaDTO findOneAnalisa(String idManrisk) {
        return null;
    }

    @Override
    public AnalisaDTO saveAnalisa(AnalisaDTO analisaDTO) {
        return null;
    }
}
