package cc.kertaskerja.manrisk_fraud.service.pemantauan;

import cc.kertaskerja.manrisk_fraud.dto.pemantauan.PemantauanReqDTO;
import cc.kertaskerja.manrisk_fraud.dto.pemantauan.PemantauanResDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PemantauanService {

    List<PemantauanResDTO> findAllPemantauan(String nip, String tahun);

    PemantauanResDTO findOnePemantauan(String idRekin);

    PemantauanResDTO savePemantauan(PemantauanReqDTO reqDTO);

    PemantauanResDTO updatePemantauan(String idRekin, PemantauanReqDTO reqDTO);

    PemantauanResDTO verifyPemantauan(String idRekin, PemantauanReqDTO.UpdateStatusDTO updateDTO);

    void deletePemantauan(String idRekin);
}
