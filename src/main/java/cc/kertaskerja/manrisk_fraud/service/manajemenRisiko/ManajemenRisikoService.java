package cc.kertaskerja.manrisk_fraud.service.manajemenRisiko;

import cc.kertaskerja.manrisk_fraud.dto.ManajemenRisikoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ManajemenRisikoService {
    List<ManajemenRisikoDTO> showAllData(String nip, String tahun);
}
