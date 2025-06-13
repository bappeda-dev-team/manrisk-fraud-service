package cc.kertaskerja.manrisk_fraud.service.identifikasi;

import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiDTO;
import cc.kertaskerja.manrisk_fraud.entity.Identifikasi;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IdentifikasiService {
    List<IdentifikasiDTO> findAllIdentifikasi(String nip, String tahun);

    IdentifikasiDTO findOneIdentifikasi(String idManrisk);

    IdentifikasiDTO saveDataIdentifikasi(IdentifikasiDTO identifikasiDTO);
}
