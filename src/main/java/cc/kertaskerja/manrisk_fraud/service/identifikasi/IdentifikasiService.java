package cc.kertaskerja.manrisk_fraud.service.identifikasi;

import cc.kertaskerja.manrisk_fraud.dto.IdentifikasiDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IdentifikasiService {
    List<IdentifikasiDTO> findAllIdentifikasi(String nip, String tahun);

    IdentifikasiDTO findOneIdentifikasi(String idRekin);

    IdentifikasiDTO saveIdentifikasi(IdentifikasiDTO identifikasiDTO);

    IdentifikasiDTO updateIdentifikasi(String idRekin, IdentifikasiDTO identifikasiDTO);

    void deleteIdentifikasi(String idRekin);
}