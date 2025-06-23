package cc.kertaskerja.manrisk_fraud.service.hasilPemantauan;

import cc.kertaskerja.manrisk_fraud.dto.HasilPemantauanDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HasilPemantauanService {

    List<HasilPemantauanDTO> findAll(String nip, String tahun);

    HasilPemantauanDTO save(HasilPemantauanDTO dto);
}
