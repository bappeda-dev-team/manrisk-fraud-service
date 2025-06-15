package cc.kertaskerja.manrisk_fraud.service.analisa;

import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnalisaService {
    List<AnalisaDTO> findAllAnalisa(String nip, String tahun);

    AnalisaDTO findOneAnalisa(String idManrisk);

    AnalisaDTO saveAnalisa(AnalisaDTO analisaDTO);
}
