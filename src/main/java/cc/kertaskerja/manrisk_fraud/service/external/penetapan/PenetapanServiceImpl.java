package cc.kertaskerja.manrisk_fraud.service.external.penetapan;

import cc.kertaskerja.manrisk_fraud.service.global.AccessTokenService;
import cc.kertaskerja.manrisk_fraud.utils.HttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PenetapanServiceImpl implements PenetapanService {

    private final AccessTokenService accessTokenService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${integration.penetapan.base-url}")
    private String penetapanBaseUrl;

    @Override
    public List<Map<String, Object>> findAllTujuanOpd(String kodeOpd, String tahun) {
        String token = accessTokenService.getAccessToken();
        String url = String.format("%s/opd/tujuan?kodeOpd=%s&tahun=%s", penetapanBaseUrl, kodeOpd, tahun);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        Map<String, Object> response = httpClient.get(url, headers, Map.class);

        try {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            if (data != null && data.get("tujuan_opds") instanceof List) {
                Object tujuanOpdsRaw = data.get("tujuan_opds");
                String jsonData = objectMapper.writeValueAsString(tujuanOpdsRaw);
                return objectMapper.readValue(jsonData, new TypeReference<List<Map<String, Object>>>() {});
            } else {
                return Collections.emptyList();
            }
        } catch (ClassCastException | JsonProcessingException e) {
            System.err.println("Error processing JSON response: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> findDetailTujuanOpdByKode(String kodeTujuanOpd) {
        String token = accessTokenService.getAccessToken();

        String url = String.format("%s/opd/tujuan/detail/%s", penetapanBaseUrl, kodeTujuanOpd);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        return httpClient.get(url, headers, Map.class);
    }
}
