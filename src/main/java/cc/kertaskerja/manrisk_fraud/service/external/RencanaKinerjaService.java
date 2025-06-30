package cc.kertaskerja.manrisk_fraud.service.external;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RencanaKinerjaService {
    List<Map<String, Object>> findAll(String nip, String tahun);
}
