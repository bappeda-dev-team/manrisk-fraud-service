package cc.kertaskerja.manrisk_fraud.service.external.penetapan;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PenetapanService {
    List<Map<String, Object>> findAllTujuanOpd(String kodeOpd, String tahun);
    Map<String, Object> findDetailTujuanOpdByKode(String kodeTujuanOpd);
}
