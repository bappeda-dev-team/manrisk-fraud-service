package cc.kertaskerja.manrisk_fraud.service.identifikasi;

import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.identifikasi.IdentifikasiResDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IdentifikasiService {
    List<IdentifikasiResDTO> findAllIdentifikasi(String nip, String tahun);

    IdentifikasiResDTO findOneIdentifikasi(String idRekin);

    IdentifikasiResDTO saveIdentifikasi(IdentifikasiReqDTO reqDTO);

    IdentifikasiResDTO updateIdentifikasi(String idRekin, IdentifikasiReqDTO reqDTO);

    IdentifikasiResDTO verifyIdentifikasi(String idRekin, IdentifikasiReqDTO.UpdateStatusDTO updateDTO);

    void deleteIdentifikasi(String idRekin);
}