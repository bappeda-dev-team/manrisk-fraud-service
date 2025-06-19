package cc.kertaskerja.manrisk_fraud.service.analisa;

import cc.kertaskerja.manrisk_fraud.dto.AnalisaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnalisaService {
    List<AnalisaDTO> findAllAnalisa(String nip, String tahun);

    AnalisaDTO findOneAnalisa(String idRekin);

    AnalisaDTO saveAnalisa(AnalisaDTO analisaDTO);

    AnalisaDTO updateAnalisa(String idRekin, AnalisaDTO analisaDTO);

    void deleteAnalisa(String idRekin);
}
