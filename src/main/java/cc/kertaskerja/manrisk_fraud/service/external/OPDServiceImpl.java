package cc.kertaskerja.manrisk_fraud.service.external;

import cc.kertaskerja.manrisk_fraud.utils.HttpClient;
import cc.kertaskerja.manrisk_fraud.service.global.AccessTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OPDServiceImpl implements OPDService {

    private final AccessTokenService accessTokenService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${external.rekin.base-url}")
    private String rekinBaseUrl;

    @Override
    public List<Map<String, Object>> findAll() {
        String token = accessTokenService.getAccessToken();
        String url = String.format("%s/opd/findall", rekinBaseUrl);

        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        Map<String, Object> response = httpClient.get(url, headers, Map.class);

        try {
            Object dataRaw = response.get("data");
            String jsonData = objectMapper.writeValueAsString(dataRaw);

            return objectMapper.readValue(jsonData, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }
}
