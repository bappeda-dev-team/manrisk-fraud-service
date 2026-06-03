package cc.kertaskerja.manrisk_fraud.service.external.opd;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OPDService {
    List<Map<String, Object>> findAll();
}
