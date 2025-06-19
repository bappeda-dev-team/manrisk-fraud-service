package cc.kertaskerja.manrisk_fraud.service.pemantauan;

import cc.kertaskerja.manrisk_fraud.dto.PemantauanDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PemantauanService {

    List<PemantauanDTO> findAllPemantauan(String nip, String tahun);

    PemantauanDTO savePemantauan(PemantauanDTO pemantauanDTO);
}
