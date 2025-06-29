package cc.kertaskerja.manrisk_fraud.service.risikoKecurangan;

import cc.kertaskerja.manrisk_fraud.dto.risikoKecurangan.RisikoKecuranganDTO;
import cc.kertaskerja.manrisk_fraud.entity.RisikoKecurangan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RisikoKecuranganService {
    List<RisikoKecuranganDTO> findAllByJenisRisiko(String jenisRisiko);

    RisikoKecuranganDTO save(RisikoKecuranganDTO dto);

    RisikoKecuranganDTO update(Long id, RisikoKecuranganDTO dto);

    void delete(Long id);
}
