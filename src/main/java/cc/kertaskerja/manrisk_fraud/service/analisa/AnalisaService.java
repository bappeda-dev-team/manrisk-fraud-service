package cc.kertaskerja.manrisk_fraud.service.analisa;

import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.analisa.AnalisaResDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnalisaService {
    List<AnalisaResDTO> findAllAnalisa(String nip, String tahun);

    AnalisaResDTO findOneAnalisa(String idRekin);

    AnalisaResDTO saveAnalisa(AnalisaReqDTO reqDTO);

    AnalisaResDTO updateAnalisa(String idRekin, AnalisaReqDTO reqDto);

    AnalisaResDTO verifyAnalisa(String idRekin, AnalisaReqDTO.UpdateStatusDTO updateDTO);

    void deleteAnalisa(String idRekin);
}
