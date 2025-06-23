package cc.kertaskerja.manrisk_fraud.service.pemantauan;

import cc.kertaskerja.manrisk_fraud.dto.PemantauanDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PemantauanService {

    List<PemantauanDTO> findAllPemantauan(String nip, String tahun);

    PemantauanDTO findOnePemantauan(String idRekin);

    PemantauanDTO savePemantauan(PemantauanDTO pemantauanDTO);

    PemantauanDTO updatePemantauan(String idRekin, PemantauanDTO pemantauanDTO);

    PemantauanDTO updateStatusPemantauan(String idRekin, PemantauanDTO.UpdateStatusDTO updateDTO);

    void deletePemantauan(String idRekin);
}
